<template>
    <div class="node" :style="nodeStyle" @mousedown="mouseDown">
      <div class="noselect servicename">
        <slot/>
       </div>
    </div>
</template>

<script>
export default {
    props: {
        scale: Number,
        showDetails: Boolean,
        params: Object,
    },
    data () {
        return {
            drag: false,

            // this are the coordinates relative to the originCoordinates
            x: 0,
            y: 0,

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
                position: 'absolute',
                // here we transform into screen space coordinates
                top: this.params.originY + this.y*this.scale + 'px',
                left: this.params.originX + this.x*this.scale + 'px',
                width:  this.width + 'px',
                height: this.height + 'px',
                transform: 'scale(' + this.scale + ')',
            }
        }
    },
    methods: {
      mouseDown: function (event) {
                this.ofX = event.clientX*(1/this.scale) - this.x;
                this.ofY = event.clientY*(1/this.scale) - this.y;
                this.drag = true;
                console.log("yes drag")
      },
      mouseMove: function (event) {
          if(this.drag) {
              this.x = (event.clientX*(1/this.scale) - this.ofX);
              this.y = (event.clientY*(1/this.scale) - this.ofY);
          }
      },
      mouseUp: function (event) {
          this.drag = false;
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
    border: 2px solid black;
    border-radius: 20px;
    background: lightgrey;
    opacity: 0.89;
    cursor: grab;
    box-sizing: border-box;
    box-shadow: 4px 4px 8px #101010;
  }

  .node:active {
    cursor: move;
    background: green;
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

</style>
