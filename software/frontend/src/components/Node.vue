<template>
<div class="node" :style="nodeStyle"
     @mousedown.self="mouseDown"
     @mouseover="showButtons = true"
     @mouseleave="showButtons = false"
     @click.ctrl="deleteNode">
       <div
           v-if="!noIcons && showButtons && !drag"
           id="deleteIcon"
           @click="deleteNode">
           <v-icon name="minus-square" scale="2" color="red"/>
       </div>

       <div>
         <b-btn v-if="!noIcons && showButtons && !drag"
              variant="primary"
              :id="'info'+$vnode.key"
              style="position:absolute; top: -20px; left: 0px">
                <v-icon name="info" scale="1"/>
         </b-btn>
       </div>

       <div class="noselect servicename" pointer-events="none" @mousedown.self="mouseDown">
         {{ service.name }}
       </div>

       <div class="noselect draghandle"
            :style="dragStyle"
            @mousedown.self="dummy ? mouseDown($event) : startDrag($event)"
            @mouseup="endDrag"/>

           <b-popover :target="'info'+$vnode.key"
                   placement="topright"
                   title="Dienst Informationen"
                   triggers="hover focus"
                   >
          <div> name: {{ service.name }} </div>
          <div> version: {{ service.version }} </div>
          <div> organisation: {{ service.organisation }} </div>
          <div> created: {{ service.date }} </div>
          <div> in:
            <li v-for="input in service.formatIn">
              {{
                 input.type +
                 (input.compatibilityDegree==="flexible" ? '<=' : '=') +
                 (input.version==="" ? '?' : input.version )
              }}
            </li>
          </div>
          <div> out:
            <li v-for="output in service.formatOut">
              {{
                 output.type +
                 (output.compatibilityDegree==="flexible" ? '<=' : '=') +
                 (output.version==="" ? '?' : output.version )
              }}
            </li>
          </div>
        </b-popover>

    </div>
</template>

<script>
export default {
    props: {
        showDetails: Boolean,
        noIcons: Boolean,
        params: Object,
        service: Object,
        dummy: Boolean,
        ix: Number,
        iy: Number
    },
    data () {
        return {
            drag: false,
            showButtons: false,
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
                transformOrigin: '0 0'
            }
        },
        dragStyle: function () {
            return {
                backgroundImage: 'url(/static/logos/' + this.service.logo + ')',
                backgroundSize: 'contain'
            }
        }
    },
    methods: {
      mouseUp: function () {
          this.drag = false;
      },
      mouseDown: function (event) {
          this.drag=true;
          this.$emit('mouseDown', {x: this.ix, y: this.iy, id: this.$vnode.key, serviceId: this.service.id, clientX: event.clientX, clientY: event.clientY});
      },
      startDrag: function (event) {
          this.drag=true;
          this.$emit('startDrag', this.$vnode.key);
      },
      endDrag: function (event) {
          this.drag=true;
          this.$emit('endDrag', this.$vnode.key);
      },
      deleteNode: function (event) {
          this.$emit('deleteNode', this.$vnode.key);
      }
    },
    mounted () {
        document.documentElement.addEventListener('mouseup', this.mouseUp, true)
    },
    beforeDestroy () {
        document.documentElement.removeEventListener('mouseup', this.mouseUp, true)
    }
}
</script>

<style scoped>
.node {
  border: 4px solid black;
  border-radius: 20px;
  background: #ececec;
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

#deleteIcon {
    position: absolute;
    left: 180px;
    top: -20px;
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
  font-size: 20px;

  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.draghandle {
    background: #9a9a9a;
    margin: 0 auto;
    margin-top: 10px;
    border: 2px solid black;
    border-radius: 5px;
    width:  120px;
    height: 120px;
    background-repeat: no-repeat;
}
.draghandle:active {
    background: orange;
}

</style>
