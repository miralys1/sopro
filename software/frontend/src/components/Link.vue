<template>
  <path
    :d="svgPath"
    marker-mid="url(#arrow)"
    @click.ctrl="deleteLink"
    :style="style">
    <marker id="arrow" markerWidth="10" markerHeight="10" refX="0" refY="2" orient="auto-start-reverse" markerUnits="strokeWidth">
        <text x="0" y="0" class="small">FBX</text>
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
        compability: Object,
        end: Object,

        // if set we use this cords instead of the position of the second node
        endCords: Object
    },
    computed: {
        style: function () {
            if(this.compability!==null) {
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
            } else
                return {
                        stroke: 'rgb(0,0,0)',
                        strokeWidth: 20 * this.params.scale
                }
        },
        x1: function () {
            // 100 magical value
            return this.params.originX + this.params.scale * this.start.x
        },
        x2: function () {
            return (this.endCords
                    ? this.endCords.x
                    : this.params.originX + this.params.scale * this.end.x);

        },
        y1: function () {
            return this.params.originY + this.params.scale * this.start.y;
        },
        y2: function () {
            return (this.endCords
                    ? this.endCords.y - 80
                    : this.params.originY + this.params.scale * this.end.y);
        },
        svgPath: function () {
            return "M " + this.x1 + ',' + this.y1 +
                " l "+ ((this.x2-this.x1)/2) + ',' + ((this.y2-this.y1)/2) +
                " L " + this.x2 + ',' + this.y2;
        }
    },
    data () {
        return {
            comp: null
        }
    },
    // watch: {
    //     compability: function () {
    //         if(this.compability===null) {
    //             this.comp
    //         }
    //     }
    // },
    methods: {
        deleteLink: function (event) {
            console.log("delete link")
            this.$emit('deleteLink', this.$vnode.key)
        },
        checkCompability: function (event) {
            axios.get()
        }
    },
    mounted () {
    },
    beforeDestroy () {
    }
}
</script>

<style scoped>
</style>
