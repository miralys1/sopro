<template>
<transition name="slide-fade">
    <div class="sidebar" :style="sideStyle">
        <b-form-textarea
            id="queryfield"
            v-model="query"
            placeholder="Filter for tags, names, format..."
            :no-resize="true"
            :rows="1"
            :max-rows="2">
        </b-form-textarea>
        <div style="position:relative;top:30px">
          <Node :params="{originX: 0, originY: 0, scale: 0.8}"
                :noHandles="true"
                :ix="0"
                :iy="0"
          > 3D-Modeller </Node>
          <Node :params="{originX: 0, originY: 0, scale: 0.8}"
                :noHandles="true"
                :ix="0"
                :iy="230"
          > Simulation </Node>
        </div>
    </div>
</transition>
</template>

<script>
import Node from '@/components/Node'

export default {
    components: {
        Node
    },
    computed: {
        sideStyle: function () {
            return {
                position: 'absolute',
                // here we transform into screen space coordinates
                width:  400 + 'px',
                height: 91 + 'vh'
            }
        }
    },
    data () {
        return {
            query: ''
        }
    }
}
</script>

<style>
  .sidebar {
    border: 1px solid lightgrey;
    border-radius: 5px;
    background: white;
    opacity: 1;
    cursor: grab;
    box-sizing: border-box;
    box-shadow: 3px 3px 4px #505050;
  }

  .slide-fade-enter-active {
    transition: all .1s ease;
  }
  .slide-fade-leave-active {
    transition: all .1s cubic-bezier(1.0, 0.5, 0.8, 1.0);
  }
  .slide-fade-enter, .slide-fade-leave-to
  /* .slide-fade-leave-active below version 2.1.8 */ {
    transform: translateX(10px);
    opacity: 0;
  }


</style>
