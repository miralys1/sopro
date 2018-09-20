<template>
<!-- Owner: 52 -->
<!-- TODO self prevents that both canvas and nodes are dragged at the same time -->
<div class="editor"
     @mousedown.self="mouseDown"
     @mouseup="mouseUp"
     @wheel.self="wheelEvent"
     :style="editorStyle">
    <Node v-for="node in nodes"
          :params="params"
          :key="node.id"
          :service="node.sendService"
          :ix="node.x"
          :iy="node.y"
          @startDrag="startDrag"
          @mouseDown="startNodeDrag"
          @deleteNode="handleDeleteNode"
          @wheel="wheelEvent"
          @endDrag="endDrag">
    </Node>

    <div
        v-if="options.showOrigin"
        id="origin"
        :style="originStyle"
        class="noselect"
      >
      origin
    </div>

    <svg
         width="100%"
         height="100%"
         @mousedown.self="mouseDown"
         @mouseup.self="mouseUp"
         @wheel="wheelEvent">
    <defs>
        <marker id="arrow-compatible" markerWidth="10" markerHeight="10" refX="0" refY="2" orient="auto-start-reverse" markerUnits="strokeWidth">
        <path d="M0,0 L0,4 L3,2 z" fill="#28a745" />
        </marker>

        <marker id="arrow-alternative" markerWidth="10" markerHeight="10" refX="0" refY="2" orient="auto-start-reverse" markerUnits="strokeWidth">
        <path d="M0,0 L0,4 L3,2 z" fill="#ffc107" />
        </marker>

        <marker id="arrow-incompatible" markerWidth="10" markerHeight="10" refX="0" refY="2" orient="auto-start-reverse" markerUnits="strokeWidth">
        <path d="M0,0 L0,4 L3,2 z" fill="#dc3545" />
        </marker>

        <marker id="arrow-non" markerWidth="10" markerHeight="10" refX="0" refY="2" orient="auto-start-reverse" markerUnits="strokeWidth">
        <path d="M0,0 L0,4 L3,2 z" fill="#000000" />
        </marker>
    </defs>
    <Link v-for="link in linkcoords"
          :compatibility="link.compatibility"
          :params="params"
          :dummy="false"
          :start="link.start"
          :end="link.end"
          :key="link.id + '-link'"
          @deleteLink="handleDeleteLink"
          @mouseup.self="mouseUp"
          @wheel.self="wheelEvent"
          @showAlternative="showLinkAlt"
          @gotComp="setLinkComp"
          />

    <!-- TODO endCords with end object -->
    <Link
        v-if="newLinkCords!==null"
        :params="params"
        :dummy="true"
        :start="nodes.find(e => e.id === dragLink)"
        :endCords="newLinkCords"
        style="z-index: 2;opacity: 0.4;"
    />
  </svg>
  <SidePanel
        v-if="services!==null"
        v-show="sidePanelShow"
        style="position:absolute;top:0px;left:0px"
        :services="services"
        @newNode="createNewNode"
  />
  <b-button-toolbar style="position:absolute;top:10px;right: 40px;" key-nav aria-label="Editor toolbar">
  <b-button-group class="mx-1">
    <b-button :pressed.sync="sidePanelShow" variant="primary">
        <v-icon
          name="columns"
          scale="1.7"
        />
    </b-button>
  </b-button-group>
  <b-button-group class="mx-1">
    <b-button @click="zoom(0.2)" variant="primary">
        <v-icon
          name="search-plus"
          scale="1.7"
        />
    </b-button>
    <b-button @click="scale=1" variant="primary">
      reset
    </b-button>
    <b-button @click="zoom(-0.2)" variant="primary">
        <v-icon
          name="search-minus"
          scale="1.7"
        />
    </b-button>
  </b-button-group>
  <b-button-group class="mx-1">
    <b-button @click="save" variant="success">
        <v-icon
          name="save"
          scale="1.7"
        />
    </b-button>
    <b-dropdown id="ddown-right" class="mx-1" right>
      <span slot="text">
          <v-icon
          name="cog"
          scale="1.7"
          />
      </span>
      <EditorSettings
        v-if="gotData"
        :compId="$route.params.compId"
        :owner="isOwner"
        @optionsChanged="options=$event"/>
    </b-dropdown>
  </b-button-group>
  </b-button-toolbar>

  <Node
      v-if="insertingNode"
      :params="{originX: 0, originY: 0, scale: scale}"
      style="z-index: 2;opacity: 0.4;border: 4px dotted black; background-color: lightgreen"
      noIcons="true"
      :service="services.find(e => e.id==newNodeId)"
      :ix="newNodeX"
      :iy="newNodeY"
  />

  <b-modal ref="alternativeModal" title="Alternatives available!">
        <b-container fluid>
        <b-row class="mb-1">
          <b-col v-for="alt in alternative">
                <Node :key="alt.ids[0]"
                       noIcons="true"
                       :params="{originX: 0, originY: 0, scale: 1}"
                       :service="services.find(e => e.id == alt.ids[0])"
                       :dummy="true"
                       :ix="0"
                       :iy="0"
                       @mouseDown="createNewNode"
                       />
          </b-col>
         </b-row>
        </b-container>
  </b-modal>
</div>
</template>
<script>
import SidePanel from '@/components/SidePanel'
import Node from '@/components/Node'
import Link from '@/components/Link'
import EditorSettings from '@/components/EditorSettings'

function newId(list) {
    if (list===null || list===undefined || list.length===0) return 1;
    return (Math.max(...list.map(obj => obj.id)) + 1)
}

export default {
  components: {
    Link, SidePanel, Node, EditorSettings
  },
    // TODO Short style syntax save () { ... }
  computed: {
    editorStyle: function () {
        var x = this.originX;
        var y = this.originY;
        return {
            backgroundSize: 100*this.scale + 'px ' + 100*this.scale + 'px',
            backgroundPosition: x + 'px ' + y + 'px'
        }
    },
    originStyle: function () {
        return {
            position: 'absolute',
            left: this.originX + 'px',
            top: this.originY + 'px',
            transform: 'scale('+ this.scale + ')',
            transformOrigin: '0 0'
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
            compatibility: ls.compatibility,
            start: this.nodes.find(n => n.id == ls.node1),
            end: this.nodes.find(n => n.id == ls.node2),
            id: ls.id
        }))
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
          options: {
              showOrigin: true,
          },

          insertingNode: false,

          isOwner: false,
          newNodeX: 0,
          newNodeY: 0,
          newNodeId: null,

          // TODO Use server model
          nodes: [],
          composition: null,
          services: null,
          scale: 1,
          links: [],

          gotData: false,

          // we haven't got something like event.deltaX
          // so we need to calculate that ourselfes
          lastX: 0,
          lastY: 0,

          ofX: 0,
          ofY: 0,

          newLinkCords: null,
          alternative: null
      }
  },
  methods: {
      wheelEvent: function (event) {
          console.log("scale: " + this.scale)
          this.zoom(5/event.deltaY);
      },
      zoom: function (factor) {
          if(this.scale + factor >= 0.15
             && this.scale + factor <= 7) {
                this.scale = this.scale + factor;
          }
      },
      mouseDown: function (event) {
          this.dragCanvas=true; // TODO switch back
          this.lastX=event.clientX;
          this.lastY=event.clientY;
      },
      mouseUp: function(event) {
          this.dragCanvas=false;
          this.dragNode=null;
          this.newLinkCords = null;
          this.dragLink=null;
          if(this.insertingNode) {
            // TODO real ids
            this.nodes = this.nodes.concat({id: newId(this.nodes),
                sendService: (this.services.find(e => e.id==this.newNodeId)),
                x: (event.clientX - this.originX)*1/this.scale,
                y: (event.clientY - this.originY - 80)*1/this.scale})

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
              this.nodes = this.nodes.map(el =>
                                el.id != this.dragNode
                                ? el
                                : ({x: newX, y: newY, id: el.id, sendService: el.sendService}))
          }
          if(this.insertingNode) {
              this.newNodeX = (event.clientX*(1/this.scale))
              this.newNodeY = ((event.clientY-80)*(1/this.scale))
          }
          if(this.dragLink!==null) {
              this.newLinkCords = { x: event.clientX,
                                    y: event.clientY }
          }
      },
      startNodeDrag: function (event) {
          this.ofX = event.clientX*(1/this.scale) - event.x;
          this.ofY = event.clientY*(1/this.scale) - event.y;
          this.dragNode=event.id;
      },
      createNewNode: function (event) {
          this.newNodeX = (event.clientX*(1/this.scale))
          this.newNodeY = ((event.clientY - 80) *(1/this.scale))
          this.newNodeId = event.serviceId
          this.insertingNode = true
          this.sidePanelShow = false
      },
      handleDeleteLink: function (event) {
          this.links = this.links.filter(e => (e.id + '-link') != event)
      },
      handleDeleteNode: function (event) {
          this.nodes = this.nodes.filter(e => e.id != event)
          this.links = this.links.filter(e => e.node1 != event && e.node2 != event)
      },
      setLinkComp: function (event) {
          this.links.find(e => (e.id + '-link')===event.id).compatibility = event.comp;
      },
      startDrag: function (id) {
          this.dragLink = id;
      },
      endDrag: function (id) {
          if (this.dragLink!==null) {
            var n1 = this.dragLink;
            var n2 = id;
            this.dragLink = null;
            // TODO use put
            if (this.links.find(e => e.node1 === n1 && e.node2 === n2)===undefined) {
                this.links = this.links.concat({id: newId(this.links), node1: n1, node2: n2, compatibility: null});
            }
          }
      },
      showLinkAlt: function (event) {
          this.alternative = event
          this.$refs.alternativeModal.show()
      },
      save: function (event) {
          var nodes = this.nodes;
          var comps = this.composition;
          var links = this.links.map(function (e) {
                  return {
                          id: e.id,
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
                    this.isOwner = response.data.isOwner
                    this.composition = response.data
                    this.nodes = this.composition.nodes //.map(e => ({id: e.id, x: e.x, y: e.y, sendService: e.sendService}))
                    this.links = this.composition.edges.map(e => ({id: e.id, node1: e.source.id, node2: e.target.id, compatibility: e.compatibility}))
                    this.gotData = true
                }
               )
          .catch(error => console.log(error))

    document.documentElement.addEventListener('mousemove', this.mouseMove, true)
    this.originX = this.$el.clientWidth / 2
    this.originY = this.$el.clientHeight / 2
  },
  beforeDestroy () {
    document.documentElement.removeEventListener('mousemove', this.mouseMove, true)
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

#origin {
    border-width: 3px 1 1 1;
    border-color: black;
    border-style: solid;
    width: 10px;
    height: 10px;
    background-color:black;
}

.editor:active {
    cursor: grabbing;
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
</style>
