<template>
<!-- Owner: 52 -->
<!-- self prevents that both canvas and nodes are dragged at the same time -->
<div class="editor" @mousedown.self="mouseDown" @wheel.self="wheelEvent" :style="editorStyle">
    <Node v-for="node in nodes"
          :params="params"
          :key="node.id"
          :service="node.sendService"
          :ix="node.x"
          :iy="node.y"
          @startDrag="startDrag"
          @mouseDown="startNodeDrag"
          @endDrag="endDrag">
    </Node>
  <svg width="100%" height="100%" pointer-events="none">
    <defs>
        <marker id="arrow" markerWidth="10" markerHeight="10" refX="0" refY="2" orient="auto-start-reverse" markerUnits="strokeWidth">
        <path d="M0,0 L0,4 L3,2 z" fill="#dc3545" />
        </marker>
    </defs>
    <Link v-for="link in linkcoords"
          :params="params"
          :start="link.start"
          state="invalid"
          :end="link.end"
          :key="link.id + '-link'"
          />
  </svg>
  <SidePanel
        v-show="sidePanelShow"
        style="position:absolute;top:0px;left:0px"
        :services="services"
        @newNode="createNewNode"
  />
  <b-button-toolbar style="position:absolute;top:10px;left:80vw" key-nav aria-label="Editor toolbar">
  <b-button-group class="mx-1">
      <b-button :pressed.sync="sidePanelShow" variant="primary">toggle Sidebar</b-button>
      <b-button @click="scale=1" variant="primary">reset zoom</b-button>
  </b-button-group>
  <b-button-group class="mx-1">
      <b-button @click="save" variant="success">Save and Exit</b-button>
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
      :params="{originX: -100, originY: -170, scale: scale}"
      style="z-index: 2;opacity: 0.4;border: 4px dotted black; background-color: lightgreen"
      :service="(services.filter(e => e.id==newNodeId))[0]"
      :ix="newNodeX"
      :iy="newNodeY"
  />
  <!-- <Link -->
  <!--     v-if="dragLink!==null" -->
  <!--     :params="params" -->
  <!--     style="z-index: 2;opacity: 0.4;border: 4px dotted black; background-color: lightgreen" -->
  <!-- /> -->
  <v-icon v-if="showTrash" class="trash" name="trash" scale="6"/>
</div>
</template>
<script>
import SidePanel from '@/components/SidePanel'
import Node from '@/components/Node'
import Link from '@/components/Link'

function newId(list) {
    if (list===null || list===undefined || list.length===0) return 0;
    console.log((e => e < 0 ? e-1 : e*-1) (Math.min(...list.map(obj => obj.id))))
    return (e => e <= 0 ? e-1 : e*(-1)) (Math.min(...list.map(obj => obj.id)))
}

export default {
  components: {
    Link, SidePanel, Node
  },
  computed: {
    editorStyle: function () {
        var x = this.originX+100;
        var y = this.originY+100;
        return {
            backgroundSize: 100*this.scale + 'px ' + 100*this.scale + 'px',
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
            start: {x: this.nodes.find(n => n.id == ls.node1).x,
                    y: this.nodes.find(n => n.id == ls.node1).y},
            end:   {x: this.nodes.find(n => n.id == ls.node2).x,
                    y: this.nodes.find(n => n.id == ls.node2).y},
                    id: ls.id }))
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
          showTrash: false,

          insertingNode: false,

          newNodeX: 0,
          newNodeY: 0,
          newNodeId: null,

          // TODO Use server model
          nodes: [],
          composition: null,
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
          if(this.scale + 5/event.deltaY >= 0.15
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
          this.showTrash=false;
          if(this.insertingNode) {
            // TODO real ids
            console.log("new node inserted")
            // TODO Magic numbers REAL serviceId
            this.nodes = this.nodes.concat({id: newId(this.nodes), sendService: (this.services.find(e => e.id==this.newNodeId)),
                                            x: (event.clientX - this.originX - 100)*1/this.scale,
                                            y: (event.clientY - this.originY - 170)*1/this.scale})

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
              this.nodes = this.nodes.map(el => el.id != this.dragNode ? el : ({x: newX, y: newY, id: el.id, sendService: el.sendService}))
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
          this.showTrash=true;
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
          var n1 = this.dragLink;
          var n2 = id;
          this.dragLink = null;
          // TODO Prevent double entries
          // TODO Get service connecting
          // console.log(newId(this.links))
          this.links = this.links.concat({id: newId(this.links),node1: n1, node2: n2, compatibility: null});
      },
      save: function (event) {
          var nodes = this.nodes.map(function(e) {
                  return {
                      id: (e.id <= 0 ? 0 : e.id),
                      x: e.x,
                      y: e.y,
                      sendService: e.sendService
                  }
              });
          var comps = this.composition;
          var links = this.links.map(function (e) {
                  return {
                          id: (e.id <= 0 ? 0 : e.id),
                          source: nodes.find(n => n.id == e.node1),
                          target: nodes.find(n => n.id == e.node2)
                  }
              });

          var dataSeg = {
              owner: comps.owner,
              id: comps.id,
              name: comps.name,
              nodes: nodes,
              edges: links,
              editable: comps.editable
          }

          this.axios.put('/compositions/' + this.$route.params.compId,
                         dataSeg,
                         {headers: {"Content-Type": "application/json" }}
                        )
      }
  },
  mounted () {
    this.axios.get('/services')
        .then(response => this.services = response.data)
        .catch(error => console.log(error))

    this.axios.get('/compositions/' + this.$route.params.compId)
          .then(response => {
                    this.composition = response.data
                    this.nodes = this.composition.nodes //.map(e => ({id: e.id, x: e.x, y: e.y, sendService: e.sendService}))
                    this.links = this.composition.edges.map(e => ({id: e.id, node1: e.source.id, node2: e.target.id}))
                }
               )
          .catch(error => console.log(error))

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
    top: 8vh;
    height:92vh;
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

.trash {
    position: absolute;
    top: 75vh;
    left: 92vw;
}
</style>
