<template>
<multipane class="vertical-panes" layout="vertical">
  <div class="pane" :style="{ minWidth: '0%', width: '150px', maxWidth: '40%' }">
    <div>
      <h6 class="title is-6">Janek ist dum</h6>
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

export default {
  name: 'HelloWorld',
  data () {
    return {
      msg: 'Welcome to Your Vue.js App'
    }
  },

  components: {
    draggable,
    Multipane,
    MultipaneResizer
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
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
  width: 100%;
  height: 400px;
  border: 1px solid #ccc;
}
.vertical-panes > .pane {
  text-align: left;
  padding: 15px;
  overflow: hidden;
  background: #eee;
}
.vertical-panes > .pane ~ .pane {
  border-left: 1px solid #ccc;
}

</style>
