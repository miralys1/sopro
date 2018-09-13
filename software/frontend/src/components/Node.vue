<template>
    <div class="node" :style="nodeStyle" @mousedown.self="mouseDown">
      <div class="noselect servicename" pointer-events="none">
        {{ service.name }}
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
        service: Object,
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
          this.$emit('mouseDown', {x: this.ix, y: this.iy, id: this.$vnode.key, serviceId: this.service.id, clientX: event.clientX, clientY: event.clientY});
      },
      startDrag: function (event) {
          this.$emit('startDrag', this.$vnode.key);
      },
      endDrag: function (event) {
          this.$emit('endDrag', this.$vnode.key);
      }
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
  box-shadow: 0px 4px 3px #101010;
}

.node:active {
  cursor: move;
  background: lightgreen;
  box-shadow: 0px 8px 3px #101010;
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

  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
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
