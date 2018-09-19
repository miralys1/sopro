<template>
<transition name="slide-fade">
    <div class="noselect sidebar" :style="sideStyle">
      <b-form-textarea
            id="queryfield"
            v-model="query"
            placeholder="Filter for tags, names, format..."
            :no-resize="true"
            :rows="1"
            :max-rows="2">
        </b-form-textarea>
        <b-container class="nodegrid">
        <b-row>
          <b-col v-for="service in result" >
            <Node :params="{originX: 0, originY: 0, scale: 0.9}"
                    :noHandles="false"
                    :service="service"
                    :key="service.id"
                    :dummy="true"
                    :ix="0"
                    :iy="0"
                    @mouseDown="dragNode"
                  >
            </Node>
          </b-col>
         </b-row>
        </b-container>
    </div>
</transition>
</template>

<script>
import Node from '@/components/Node'

export default {
    components: {
        Node
    },
    props: {
        services: Array
    },
    computed: {
        sideStyle: function () {
            return {
                position: 'absolute',
                // here we transform into screen space coordinates
                width:  500 + 'px',
                height: 92 + 'vh'
            }
        }
    },
    watch: {
        query: function () {
            console.log(this.query);
            if(this.query=='') this.result = this.services;
            else return this.$search(this.query, this.services, this.options)
                        .then(e => this.result = e);
        }
    },
    data () {
        return {
            query: '',
            result: this.services,

            options: {
                shouldSort: true,
                threshold: 0.6,
                location: 0,
                distance: 100,
                maxPatternLength: 32,
                minMatchCharLength: 1,
                keys: [
                    "name",
                    "organisation",
                    "formatIn.type",
                    "formatOut.type"
                ]
            }
        }
    },
    methods: {
        dragNode: function (event) {
           this.$emit('newNode', event)
        }
    }
}
</script>

<style>
.sidebar {
  border: 1px solid lightgrey;
  border-radius: 5px;
  background: white;
  overflow: auto;
  opacity: 1;
  cursor: grab;
  box-sizing: border-box;
  box-shadow: 3px 3px 4px #505050;
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

.nodegrid {
    margin-top: 20px;
}
</style>
