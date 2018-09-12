<template>
    <div class="node" :style="nodeStyle" @mousedown.self="mouseDown">
      <div class="noselect servicename" pointer-events="none">
        <slot pointer-events="none"/>
       </div>
       <div v-if="!noHandles" class="noselect draghandle" @mousedown.self="startDrag" @mouseup="endDrag"/>
    </div>
</template>

<script>
export default {
    props: {
        showDetails: Boolean,
        noHandles: Boolean,
        params: Object,
        serviceId: Number,
        dummy: Boolean,
        ix: Number,
        iy: Number
    },
    data () {
        return {
            drag: false,
            height: 200,
            width: 200,

            /* We cant just move the element to the position of the mouse
               We have to take into consideration the place where we grabbed
               the node */
            ofX: 0,
            ofY: 0
        }
    },
    computed: {
        nodeStyle: function () {
            return {
                position: (this.dummy ? 'relative' : 'absolute'),
                // here we transform into screen space coordinates
                top: this.params.originY + this.iy*this.params.scale + 'px',
                left: this.params.originX + this.ix*this.params.scale + 'px',
                width:  this.width + 'px',
                height: this.height + 'px',
                transform: 'scale(' + this.params.scale + ')',
            }
        }
    },
    methods: {
      mouseDown: function (event) {
                this.ofX = event.clientX*(1/this.params.scale) - this.ix;
                this.ofY = event.clientY*(1/this.params.scale) - this.iy;
                this.drag = true;
                console.log("yes drag")
      },
      mouseMove: function (event) {
          if(this.drag) {
              var newX = (event.clientX*(1/this.params.scale) - this.ofX);
              var newY = (event.clientY*(1/this.params.scale) - this.ofY);
              this.$emit('updatePos', ({x: newX, y: newY ,id: this.$vnode.key}));
          }
      },
      mouseUp: function (event) {
          this.drag = false;
      },
      startDrag: function (event) {
          this.$emit('startDrag', this.$vnode.key);
      },
      endDrag: function (event) {
          this.$emit('endDrag', this.$vnode.key);
      }
    },
    mounted () {
        document.documentElement.addEventListener('mousemove', this.mouseMove, true)
        document.documentElement.addEventListener('mouseup', this.mouseUp, true)
    },
    beforeDestroy () {
        document.documentElement.removeEventListener('mousemove', this.mouseMove, true)
        document.documentElement.removeEventListener('mouseup', this.mouseUp, true)
    }
}
</script>

<style scoped>
.node {
  border: 4px solid black;
  border-radius: 20px;
  background: lightgrey;
  opacity: 1;
  cursor: grab;
  box-sizing: border-box;
  box-shadow: 4px 4px 8px #101010;
}

.node:active {
  cursor: move;
  border: 6px dotted darkgreen;
  box-shadow: 18px 18px 26px #101010;
  z-index: 1;
}

.noselect {
  -webkit-touch-callout: none; /* iOS Safari */
  -webkit-user-select: none; /* Safari */
  -khtml-user-select: none; /* Konqueror HTML */
  -moz-user-select: none; /* Firefox */
  -ms-user-select: none; /* Internet Explorer/Edge */
  user-select: none; /* Non-prefixed version, currently
                                supported by Chrome and Opera */
}

.servicename {
  color: black;
  text-align: center;
  font-size: 30px;
}

.draghandle {
    background: lightblue;
    margin: 0 auto;
    margin-top: 40px;
    border: 2px solid black;
    border-radius: 100%;
    width:  50px;
    height: 50px;
}
.draghandle:active {
    background: orange;
}

</style>
