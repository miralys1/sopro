<template>
  <path
          :d="svgPath"
          marker-mid="url(#arrow)"
          :style="style">
        <!-- <animate attributeType="CSS" attributeName="opacity" -->
        <!--             from="1" to="0.7" dur="10s" repeatCount="indefinite" /> -->
            <marker id="arrow" markerWidth="10" markerHeight="10" refX="0" refY="2" orient="auto-start-reverse" markerUnits="strokeWidth">
            <path d="M0,0 L0,4 L3,2 z" :fill="(state==="invalid" ? '#dc3545' : '#28a745')" />
            </marker>
  </path>
</template>

<script>
function invalidStyle () {
}

export default {
    props: {
        // containing origin and more
        params: Object,
        // start and end of link
        start: Object,
        end: Object,
        type: String,
        state: String, // Should be either "valid", "invalid", "alternative"
        format: Object // Format Object through which Nodes are connected
        // TEST: If state is invalid or alternative format should be null or undefined
    },
    computed: {
        style: function () {
            if(this.state!==null) {
                switch (this.state) {
                case "valid":
                    return {
                        stroke: '#28a745',
                        strokeWidth: 20 * this.params.scale
                    }
                case "invalid":
                    return {
                        stroke: '#dc3545',
                        strokeWidth: 20 * this.params.scale,
                        // filter: "url(#glow)"
                    }
                case "alternative":
                    return {
                        stroke: '#ffc107',
                        strokeWidth: 20 * this.params.scale
                    }
                default:
                    return {
                        stroke: 'rgb(70,70,70)',
                        strokeWidth: 20 * this.params.scale
                    }
                }
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
        },
        svgPath: function () {
            return "M " + this.x1 + ',' + this.y1 +
                " l "+ ((this.x2-this.x1)/2) + ',' + ((this.y2-this.y1)/2) +
                " L " + this.x2 + ',' + this.y2;
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
</style>
