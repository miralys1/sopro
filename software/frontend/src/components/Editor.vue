<template>

<multipane class="vertical-panes" layout="vertical">
  <div class="pane" :style="{ minWidth: '0%', width: '25%', maxWidth: '100%' }">
    <div>
      <SidePanel style="float: left; width: 100%; padding-right: 10px"/>
    </div>
  </div>
  <multipane-resizer></multipane-resizer>
  <div class="pane" :style="{ width: '100%', maxWidth: '100%' }">
    <div>
      <h6 class="title is-6">Pane 2</h6>
    </div>
  </div>
</multipane>
</template>
<script>
import { Multipane, MultipaneResizer } from 'vue-multipane'
import draggable from 'vuedraggable'
import SidePanel from '@/components/SidePanel'


export default {
  props: {
    compId: Number
  },
  components: {
    draggable, Multipane, MultipaneResizer, SidePanel
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

</style>
