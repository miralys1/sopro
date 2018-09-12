<template>
  <div class="mainlayout">
    <b-container style="text-align: center">
      <h3 class="font-weight-bold">bearbeitbare Kompositionen</h3>
      <b-row class="comprow">
        <b-col v-for="comp in editableComps"
               :key="comp.id"
               class="compcol round">
          {{comp.name}} <br/>
          ID: {{comp.id}} <br/>
          <b-btn :to="{ name: 'Editor' , params: { compId: comp.id }}"
                 class="edit"
                 style="float:right"
                 size="sm"
                 variant="">edit</b-btn>
        </b-col>
      </b-row>
    </b-container>
    <b-container style="text-align: center">
      <h3 class="font-weight-bold"
          style="margin-top: 5%">einsehbare Kompositionen</h3>
      <b-row class="comprow">
        <b-col v-for="comp in viewableComps"
               :key="comp.id"
               class="compcol round">
          {{comp.name}} <br/>
          ID: {{comp.id}} <br/>
        </b-col>
      </b-row>
    </b-container>
    <b-container style="text-align: center">
      <h3 class="font-weight-bold"
          style="margin-top: 5%">öffentliche Kompositionen</h3>
      <b-row class="comprow">
        <b-col v-for="comp in publicComps"
               :key="comp.id"
               class="compcol round">
          {{comp.name}} <br/>
          ID: {{comp.id}} <br/>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>

export default {
  data() {
    return {
      editableComps: [
        {
          name: 'bearbeitbare Komposition 1',
          id: 0,
          isEditable: true
        },
        {
          name: 'bearbeitbare Komposition 2',
          id: 1,
          isEditable: true
        },
        {
          name: 'bearbeitbare Komposition 3',
          id: 2,
          isEditable: true
        },
        {
          name: 'bearbeitbare Komposition 4',
          id: 3,
          isEditable: true
        },
        {
          name: 'bearbeitbare Komposition 5',
          id: 4,
          isEditable: true
        },
        {
          name: 'bearbeitbare Komposition 6',
          id: 5,
          isEditable: true
        }
      ],
      viewableComps: [
        {
          name: 'einsehbare Komposition 1',
          id: 6,
          isEditable: false
        },
        {
          name: 'einsehbare Komposition 2',
          id: 7,
          isEditable: false
        },
        {
          name: 'einsehbare Komposition 3',
          id: 8,
          isEditable: false
        },
        {
          name: 'einsehbare Komposition 4',
          id: 9,
          isEditable: false
        },
        {
          name: 'einsehbare Komposition 5',
          id: 10,
          isEditable: false
        },
        {
          name: 'einsehbare Komposition 6',
          id: 11,
          isEditable: false
        }
      ],
      publicComps: [
        {
          name: 'öffentliche Komposition 1',
          id: 12,
          isEditable: false
        },
        {
          name: 'öffentliche Komposition 2',
          id: 13,
          isEditable: false
        },
        {
          name: 'öffentliche Komposition 3',
          id: 14,
          isEditable: false
        },
        {
          name: 'öffentliche Komposition 4',
          id: 15,
          isEditable: false
        },
        {
          name: 'öffentliche Komposition 5',
          id: 16,
          isEditable: false
        },
        {
          name: 'öffentliche Komposition 6',
          id: 17,
          isEditable: false
        },
        {
          name: 'öffentliche Komposition 7',
          id: 18,
          isEditable: false
        }
      ]
    }
  },
  mounted() {
    this.axios({
      method: 'get',
      url: 'http://134.245.1.240:9061/composer-0.0.1-SNAPSHOT/compositions',
      headers: {
        'id': 0
      }
    }).then(response => {
      this.editableComps = response.data[0]
      this.viewableComps = response.data[1]
      this.publicComps = response.data[2]
    })
    .catch(error => alert('Server currently not available'))
  }
}
</script>

<style>
h3 {
  text-align: left
}
.comprow{
  padding-left: 5%
}
.compcol {
  margin: 10px 10px;
  padding-top: 10px;
  padding-left: 10px;
  padding-right: 10px;
  padding-bottom: 10px;
  text-align: left;
  font-size: 15px;
  border: 2px solid black;
  min-width: 150px;
  max-width: 150px;
  height: 130px;
  overflow: hidden;
  position: relative;
  /* white-space:nowrap;
  text-overflow: ellipsis; */
}
.edit {
  position: absolute;
  bottom: 10px;
  right: 10px;
}
</style>
