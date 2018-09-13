<template>
<!-- self prevents that both canvas and nodes are dragged at the same time -->
<div class="editor" @mousedown.self="mouseDown" @wheel.self="wheelEvent" :style="editorStyle">
    <h6 class="title is-6"></h6>
    <Node v-if="services!==null" v-for="node in nodes"
          :params="params"
          :key="node.id"
          :service="(services.filter(e => e.id==node.serviceId))[0]"
          :ix="node.x"
          :iy="node.y"
          @startDrag="startDrag"
          @mouseDown="startNodeDrag"
          @endDrag="endDrag">
    </Node>
  <svg width="100%" height="100%" pointer-events="none">
    <Link v-for="link in linkcoords"
          :params="params"
          :start="link.start"
          :end="link.end"
          :key="link.id + '-link'"
          />
  </svg>
  <SidePanel
        v-if="sidePanelShow"
        style="position:absolute;top:0px;left:0px"
        :services="services"
        @newNode="createNewNode"
  />
  <b-button-toolbar style="position:absolute;top:20px;left:80vw" key-nav aria-label="Editor toolbar">
  <b-button-group class="mx-1">
      <b-button :pressed.sync="sidePanelShow" variant="primary">toggle Sidebar</b-button>
      <b-button @click="scale=1" variant="primary">reset zoom</b-button>
  </b-button-group>
  <b-button-group class="mx-1">
      <b-button variant="success">Save</b-button>
  </b-button-group>
  </b-button-toolbar>
        <b-form-textarea
            style="position:absolute;top:40px;left:40px"
            id="queryfield"
            v-model="query"
            placeholder="Filter for tags,names,format..."
            :rows="1"
            :max-rows="2"
  </b-form-textarea>
  <Node
      v-if="insertingNode"
      :params="{originX: -100, originY: -200, scale: scale}"
      style="z-index: 2;opacity: 0.4;border: 4px dotted black"
      :service="(services.filter(e => e.id==newNodeId))[0]"
      :ix="newNodeX"
      :iy="newNodeY"
  />
</div>
</template>
<script>
// import { Multipane, MultipaneResizer } from 'vue-multipane'
import SidePanel from '@/components/SidePanel'
import Node from '@/components/Node'
import Link from '@/components/Link'

export default {
  props: {
    compId: Number
  },
  components: {
    Link, SidePanel, Node
  },
  computed: {
    editorStyle: function () {
        var x = this.originX+100;
        var y = this.originY+100;
        return {
            backgroundSize: 100*this.scale + 'px ' + 100*this.scale + 'px ',
            backgroundPosition: x + 'px ' + y + 'px'
        }
    },
    params: function () {
        return {
            originX: this.originX,
            originY: this.originY,
            scale: this.scale
        }
    },
    linkcoords: function () {
        return this.links.map( ls => ({
                             start: {x: (this.nodes.filter(n => n.id == ls.node1)[0]).x, y: (this.nodes.filter(n => n.id == ls.node1)[0]).y},
                             end:   {x: (this.nodes.filter(n => n.id == ls.node2)[0]).x, y: (this.nodes.filter(n => n.id == ls.node2)[0]).y},
                             id: ls.id })
                        )
    }
  },
  data () {
      return {
          originX: 0,
          originY: 0,
          dragCanvas: false,
          dragNode: null,
          dragLink: null,
          sidePanelShow: true,

          insertingNode: false,

          newNodeX: 0,
          newNodeY: 0,
          newNodeId: null,

          nodes: [
              ({id: 1, serviceId: 5, x: 10, y: 10, })
          ],
          services: null,
          scale: 1,
          links: [],

          // we haven't got something like event.deltaX
          // so we need to calculate that ourselfes
          lastX: 0,
          lastY: 0,

          ofX: 0,
          ofY: 0

      }
  },
  methods: {
      wheelEvent: function (event) {
          if(this.scale + 5/event.deltaY >= 0.05
             && this.scale + 5/event.deltaY <= 7) {
                this.scale = this.scale + 5/event.deltaY;
          }
      },
      mouseDown: function (event) {
          this.dragCanvas=true; // TODO switch back
          this.lastX=event.clientX;
          this.lastY=event.clientY;
      },
      mouseUp: function(event) {
          console.log("Mouse Up")
          this.dragCanvas=false;
          this.dragNode=null;
          if(this.insertingNode) {
            // TODO real ids
            console.log("new node inserted")
            // TODO Magic numbers REAL serviceId
              this.nodes = this.nodes.concat({id: this.nodes.length+1, serviceId: this.newNodeId,
                                              x: (event.clientX - this.originX - 100)*1/this.scale,
                                              y: (event.clientY - this.originY - 200)*1/this.scale})

            this.insertingNode = false;
            this.sidePanelShow = true;
            this.newNodeId = null;
          }
      },
      mouseMove: function (event) {
          if (this.dragCanvas) {
            var deltaX = event.clientX - this.lastX;
            var deltaY = event.clientY - this.lastY;

            this.lastX = event.clientX;
            this.lastY = event.clientY;

            this.originX += deltaX;
            this.originY += deltaY;
          }
          if(this.dragNode!==null) {
              var newX = (event.clientX*(1/this.scale) - this.ofX)
              var newY = (event.clientY*(1/this.scale) - this.ofY)
              // console.log(event.x + ' ' + event.y + ' ' + this.dragNode);
              this.nodes.map(e => console.log(' ' + e.x))
              this.nodes = this.nodes.map(el => el.id != this.dragNode ? el : ({x: newX, y: newY, id: el.id, serviceId: el.serviceId}))
          }
          if(this.insertingNode) {
              this.newNodeX = (event.clientX*(1/this.scale))
              this.newNodeY = (event.clientY*(1/this.scale))
          }
      },
      startNodeDrag: function (event) {
          console.log("dragged new node " + event.x + ' ' + event.y)
          this.ofX = event.clientX*(1/this.scale) - event.x;
          this.ofY = event.clientY*(1/this.scale) - event.y;
          this.dragNode=event.id;
      },
      createNewNode: function (event) {
          console.log("Creating new node")
          this.newNodeX = (event.clientX*(1/this.scale))
          this.newNodeY = (event.clientY*(1/this.scale))
          this.newNodeId = event.serviceId
          this.insertingNode = true
          this.sidePanelShow = false
      },
      startDrag: function (id) {
          console.log("start");
          this.dragLink = id;
      },
      endDrag: function (id) {
          console.log("end");
          var n1 = id;
          var n2 = this.dragLink;
          this.dragLink = null;
          this.links = this.links.concat({id: 0,node1: n1, node2: n2});
      }

  },
  created () {
    this.axios.get('http://134.245.1.240:9061/composer-0.0.1-SNAPSHOT/services')
        .then(response => this.services = response.data)
        .catch(error => console.log(error))

    console.log(this.$data)
  },
  mounted () {
    document.documentElement.addEventListener('mousemove', this.mouseMove, true)
    document.documentElement.addEventListener('mouseup', this.mouseUp, true)
    this.originX = this.$el.clientWidth / 2
    this.originY = this.$el.clientHeight / 2
  },
  beforeDestroy () {
    document.documentElement.removeEventListener('mousemove', this.mouseMove, true)
    document.documentElement.removeEventListener('mouseup', this.mouseUp, true)
  }
}
</script>

<style scoped>
.editor {
    position:relative;
    box-sizing: border-box;
    height:90vh;
    overflow: hidden;
    border-width: 3px 1 1 1;
    border-color: grey;
    border-style: solid;

    background-color:white;
    background-image: linear-gradient(lightgrey 2px, transparent 2px),
    linear-gradient(90deg, lightgrey 2px, transparent 2px),
    linear-gradient(rgba(255,255,255,.3) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255,255,255,.3) 1px, transparent 1px);
}

.editor:active {
    cursor: grabbing;
}

</style>
