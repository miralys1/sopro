<template>
<!-- self prevents that both canvas and nodes are dragged at the same time -->
<div class="editor" @mousedown.self="mouseDown" :style="editorStyle">
    <h6 class="title is-6"></h6>
    <Node v-for="node in nodes"
          :params="params"
          :key="node.id"
          :ix="node.x"
          :iy="node.y"
          @startDrag="startDrag"
          @endDrag="endDrag"
          @updatePos="nodeMove" >
      {{ node.name }}
    </Node>
  <svg width="100%" height="100%" pointer-events="none">
    <Link v-for="link in linkcoords"
          :params="params"
          :start="link.start"
          :end="link.end"
          :key="link.id + '-link'"
          />
  </svg>
  <SidePanel style="position:absolute;top:0px;left:0px" v-if="sidePanelShow"/>
  <b-button-toolbar style="position:absolute;top:20px;left:80vw" key-nav aria-label="Editor toolbar">
    <b-button-group class="mx-1">
        <b-button :pressed.sync="sidePanelShow" variant="primary">Show Services</b-button>
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
          drag: false,
          dragNode: undefined,
          sidePanelShow: false,
          nodes: [
              ({id: 0, name: '3D-Modeller' , x: 0, y: 0, }),
              ({id: 1, name: '4D-Modeller' , x: 100, y: 200, }),
              ({id: 2, name: 'Simulation'  , x: 100, y: 900, }),
              ({id: 3, name: 'whatever'    , x: 300, y: 200, }),
              ({id: 4, name: 'whatever'    , x: 600, y: 200, }),
              ({id: 5, name: 'whatever'    , x: 800, y: 600, }),
              ({id: 6, name: 'whatever'    , x: 900, y: 900, }),
          ],
          scale: 1,
          links: [],

          // we haven't got something like event.deltaX
          // so we need to calculate that ourselfes
          lastX: 0,
          lastY: 0
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
          this.drag=true; // TODO switch back
          this.lastX=event.clientX;
          this.lastY=event.clientY;
      },
      mouseUp: function(event) {
          this.drag=false;
      },
      nodeMove: function(event) {
          console.log(event.x + ' ' + event.y + ' ' + event.id);
          this.nodes = this.nodes.map(el => el.id != event.id ? el : ({x: event.x, y: event.y, id: el.id, name: el.name}));
      },
      mouseMove: function (event) {
          if (this.drag) {
            var deltaX = event.clientX - this.lastX;
            var deltaY = event.clientY - this.lastY;

            this.lastX = event.clientX;
            this.lastY = event.clientY;

            this.originX += deltaX;
            this.originY += deltaY;
          }
      },
      startDrag: function (id) {
          console.log("start");
          this.dragNode = id;
      },
      endDrag: function (id) {
          console.log("end");
          var n1 = id;
          var n2 = this.dragNode;
          this.dragNode = undefined;
          this.links = this.links.concat({id: 0,node1: n1, node2: n2});
      }

  },
  mounted () {
    document.documentElement.addEventListener('mousemove', this.mouseMove, true)
    document.documentElement.addEventListener('mouseup', this.mouseUp, true)
    document.documentElement.addEventListener('wheel', this.wheelEvent, true)
    this.originX = this.$el.clientWidth / 2
    this.originY = this.$el.clientHeight / 2
  },
  beforeDestroy () {
    document.documentElement.removeEventListener('mousemove', this.mouseMove, true)
    document.documentElement.removeEventListener('mouseup', this.mouseUp, true)
    document.documentElement.removeEventListener('wheel', this.wheelEvent, true)
  }
}
</script>

<style scoped>
h1, h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}

.vertical-panes {
  border: 1px solid #ccc;
  position: absolute;
  top: 80px; /* Header Height */
  bottom: 0vmax; /* Footer Height */
  width: 100%;
}
.vertical-panes > .pane {
  text-align: left;
  padding: 15px;
  overflow: hidden;
  background: #eee;
}
.vertical-panes > .pane ~ .pane {
  border-left: 4px solid #ccc;
}

.editor {
    position:relative;
    box-sizing: border-box;
    height:91.8vh;
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
