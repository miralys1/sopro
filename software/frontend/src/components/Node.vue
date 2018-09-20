<template>
<div class="node" :style="nodeStyle"
     @mousedown.self="mouseDown"
     @mouseover="showButtons = true"
     @mouseleave="showButtons = false"
     @click.ctrl.exact="deleteNode">
       <div
           v-if="!noIcons && showButtons && !drag"
           id="deleteIcon"
           @click="deleteNode">
           <v-icon name="minus-square" scale="2" color="red"/>
       </div>

       <div>
         <b-popover
                 :target="'info'+$vnode.key"
                 placement="topright"
                 title="Dienst Informationen"
                 triggers="click"
                 >
              <div> name: {{ service.name }} </div>
              <div> version: {{ service.version }} </div>
              <div> organisation: {{ service.organisation }} </div>
              <div> created: {{ realDate }} </div>
              <div v-if="service.formatIn.length > 0"> inputs:
                  <li v-for="input in service.formatIn">
                  {{
                      input.type +
                      (input.version!=='' ? (input.compatibilityDegree==="flexible" ? '<=' : '=') : '') +
                      input.version
                  }}
                  </li>
              </div>
              <div v-if="service.formatOut.length > 0"> outputs:
                  <li v-for="output in service.formatOut">
                  {{
                      output.type +
                      (output.version!=='' ? (output.compatibilityDegree==="flexible" ? '<=' : '=') : '') +
                      output.version
                  }}
                  </li>
              </div>
        </b-popover>
       </div>

       <div class="noselect servicename" pointer-events="none" @mousedown.self="mouseDown">
         {{ service.name }}
       </div>

       <div class="noselect draghandle"
            :style="dragStyle"
            @mousedown.self="dummy ? mouseDown($event) : startDrag($event)"
            @mouseup="endDrag"/>

       <div class="noselect serviceversion" pointer-events="none" @mousedown.self="mouseDown">
         {{ service.version }}
       </div>
       <div v-show="!noIcons"
            :id="'info'+$vnode.key"
            class="infoicon">
          <v-icon name="info" scale="2"/>
       </div>
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
        },
        realDate: function () {
            var date = new Date(this.service.date*1000);
            console.log(this.service.date)
            console.log(date.toDateString())
            return date.toDateString()
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

.serviceversion {
  color: black;
  text-align: center;
  font-size: 20px;
  bottom: 10px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.infoicon {
    position:absolute;
    bottom: 10px;
    left: 10px;
    cursor: help;
}
.draghandle {
    background: #9a9a9a;
    margin: 0 auto;
    margin-top: 15px;
    border: 2px solid black;
    border-radius: 5px;
    width:  110px;
    height: 110px;
    background-repeat: no-repeat;
}
.draghandle:active {
    background: orange;
}

</style>
