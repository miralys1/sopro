<template>
<!-- self prevents that both canvas and nodes are dragged at the same time -->
<div class="editor" @mousedown.self="mouseDown" :style="editorStyle">
    <h6 class="title is-6"></h6>
    <Node :params="nodeParams" :scale="scale">3D Modeller</Node>
    <Node :params="nodeParams" :scale="scale">Converter</Node>
    <Node :params="nodeParams" :scale="scale">4D Modeller</Node>
    <Node :params="nodeParams" :scale="scale">Stability</Node>
</div>
</template>
<script>
import { Multipane, MultipaneResizer } from 'vue-multipane'
import SidePanel from '@/components/SidePanel'
import Node from '@/components/Node'

export default {
  props: {
    compId: Number
  },
  components: {
    Multipane, MultipaneResizer, SidePanel, Node
  },
  computed: {
    editorStyle: function () {
        var x = this.originX+100;
        var y = this.originY+100;
        return {
            backgroundSize: 50*this.scale + 'px ' + 50*this.scale + 'px ',
            backgroundPosition: x + 'px ' + y + 'px'


        }
    },
    nodeParams: function () {
        return {
            originX: this.originX,
            originY: this.originY
        }
    }
  },
  data () {
      return {
          originX: 0,
          originY: 0,
          drag: false,
          canvasNodes: [],
          scale: 1,

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
          console.log('mouseDown on Canvas');
      },
      mouseUp: function(event) {
          this.drag=false;
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

</style>
