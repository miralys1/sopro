<template>
  <line class="line"
          :x1="x1 + 'px'"
          :y1="y1 + 'px'"
          :x2="x2 + 'px'"
          :y2="y2 + 'px'"
          :style="style"/>
</template>

<script>
export default {
    props: {
        // containing origin and more
        params: Object,
        // start and end of link
        start: Object,
        end: Object,
        type: String
    },
    computed: {
        style: function () {
            return {
                stroke: 'rgb(100,100,100)',
                strokeWidth: 20 * this.params.scale
            }
        },
        x1: function () {
            console.log(this.params.originX + ' ' +  this.params.scale + ' ' + this.start.x)
            // 100 magical value
            return this.params.originX + this.params.scale * this.start.x + 100;
        },
        x2: function () {
            return this.params.originX + this.params.scale * this.end.x + 100;
        },
        y1: function () {
            return this.params.originY + this.params.scale * this.start.y + 100;
        },
        y2: function () {
            return this.params.originY + this.params.scale * this.end.y + 100;
        }
    },
    data () {
        return {
        }
    },
    methods: {
    },
    mounted () {
    },
    beforeDestroy () {
    }
}
</script>

<style scoped>
  .line {

  }
</style>
