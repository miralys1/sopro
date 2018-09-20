<template>
  <path
    :d="svgPath"
    :marker-mid="'url(#arrow-' + state + ')'"
    @click.ctrl="deleteLink"
    @click="clickArrow"
    :style="style">
  </path>
</template>

<script>
export default {
    props: {
        // containing origin and more
        params: Object,
        dummy: Boolean,
        // start and end of link
        start: Object,
        compatibility: Object,
        end: Object,

        // if set we use this cords instead of the position of the second node
        endCords: Object
    },
    computed: {
        style: function () {
            return {
                stroke: this.color,
                strokeWidth: 20 * this.params.scale,
                border: "2px solid black"
            }
        },
        color: function () {
            return (this.state==='compatible'  ? '#28a745'
                    : (this.state==='alternative'  ? '#ffc107'
                    : (this.state==='incompatible' ? '#dc3545'
                    : '#000000')))
        },
        // TODO Enum
        state: function () {
            if(!this.dummy && this.compatibility!==null) {
                if (this.compatibility.compatible) {
                    return 'compatible'
                } else if (this.compatibility.compatibleServices.length > 0)
                    return 'alternative'
                else {
                    return 'incompatible'
                }
            } else return 'non'
        },
        x1: function () {
            // 100 magical value
            return this.params.originX + this.params.scale * (this.start.x + 100);
        },
        x2: function () {
            return this.endCords
                    ? this.endCords.x
                    : this.params.originX + this.params.scale * (this.end.x + 100);

        },
        y1: function () {
            return this.params.originY + this.params.scale * (this.start.y + 100);
        },
        y2: function () {
            return (this.endCords
                    ? this.endCords.y - 80
                    : this.params.originY + this.params.scale * (this.end.y  + 100));
        },
        svgPath: function () {
            return "M " + this.x1 + ',' + this.y1 +
                " l "+ ((this.x2-this.x1)/2) + ',' + ((this.y2-this.y1)/2) +
                " L " + this.x2 + ',' + this.y2;
        }
    },
    data () {
        return {
            comp: this.compatibility
        }
    },
    mounted () {
        if(!this.dummy && this.compatibility===null) {
            this.axios.get('/services/' + this.start.sendService.id + '/' +  this.end.sendService.id)
                .then(response => this.$emit('gotComp', {id: this.$vnode.key, comp: response.data}))
                .catch(error => console.log(error))
        }
    },
    methods: {
        deleteLink: function (event) {
            this.$emit('deleteLink', this.$vnode.key)
        },
        extraInfo: function (event) {
            if(this.state==='alternative') {
                console.log(this.compatibility.compatibleServices)
            }
        },
        clickArrow: function (event) {
            if(this.state==='alternative') {
                this.$emit('showAlternative', this.compatibility.compatibleServices)
            }
        }
    }
}
</script>

<style scoped>
</style>
