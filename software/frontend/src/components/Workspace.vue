<template>
  <div class="mainlayout">
    <b-container style="text-align: center">
      <b-row v-for="i in calc(~~(compositions.length/5))" :key="i">
        <b-col v-for="j in [i, i+1, i+2, i+3, i+4]" :key="j" class="compcol round">
          {{compositions[j].name}} <br />
          ID: {{compositions[j].id}} <br />
          <b-button v-if="compositions[j].isEditable"  :to="{ name: 'Editor', params: { compId: compositions[j].id}}">bearbeiten</b-button>
          <span v-else>nicht bearbeitbar</span>
        </b-col>
      </b-row>
      <b-row class="comprow">
        <b-col v-for="j in calc2()" :key="j" class="compcol round" md="1.9">
          {{compositions[j].name}} <br />
          ID: {{compositions[j].id}} <br />
          <span v-if="compositions[j].isEditable">bearbeiten</span>
          <span v-else>nicht bearbeitbar</span>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>

export default {
  data() {
    return {
      compositions: []
    }
  },
  methods: {
    calc: function (n) {
      var x = []
      for(var i = 0; i < n; i++) {
        x.push(i*5)
      }
      return x
    },
    calc2: function () {
      var x = []
      for(var i = (~~(this.compositions.length/5)) * 5; i < this.compositions.length; i++) {
        x.push(i)
      }
      return x
    }
  },
  created() {
    this.compositions = [
      {
        name: 'Komposition 1',
        id: 0,
        isEditable: false
      },
      {
        name: 'Komposition 2',
        id: 1,
        isEditable: false
      },
      {
        name: 'Komposition 3',
        id: 2,
        isEditable: true
      },
      {
        name: 'Komposition 4',
        id: 3,
        isEditable: true
      },
      {
        name: 'Komposition 5',
        id: 4,
        isEditable: false
      },
      {
        name: 'Komposition 6',
        id: 5,
        isEditable: false
      },
      {
        name: 'Komposition 7',
        id: 6,
        isEditable: false
      },
      {
        name: 'Komposition 8',
        id: 7,
        isEditable: true
      },
      {
        name: 'Komposition 9',
        id: 8,
        isEditable: true
      },
      {
        name: 'Komposition 10',
        id: 9,
        isEditable: false
      },
      {
        name: 'Komposition 11',
        id: 10,
        isEditable: false
      },
      {
        name: 'Komposition 12',
        id: 11,
        isEditable: false
      },
      {
        name: 'Komposition 10',
        id: 9,
        isEditable: false
      },
      {
        name: 'Komposition 11',
        id: 10,
        isEditable: false
      },
      {
        name: 'Komposition 12',
        id: 11,
        isEditable: false
      }
    ]
  }
}
</script>

<style>
  .compcol {
    background-color: lightblue;
    padding: 10px 10px;
    text-align: left;
    border: 3px solid black;
    width: 200px;
    overflow:hidden;
    white-space:nowrap;
    text-overflow: ellipsis;
  }
</style>
