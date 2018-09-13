<template>
  <div class="mainlayout">
    <b-container style="text-align: center"
                 v-if="!editableComps.length == 0 && isLoggedIn">
      <h3>bearbeitbare Kompositionen</h3>
      <b-row class="comprow">
        <b-col v-for="comp in editableComps"
               :key="comp.id"
               class="compcol round">
          {{comp.name}} <br/>
          <span class="author">{{comp.owner.fullName}} <br /></span>
          <b-btn :to="{
                   name: 'Editor',
                   params: { compId: comp.id, viewerId: userId }}"
                 class="edit"
                 style="float:right"
                 size="sm"
                 variant="">edit</b-btn>
        </b-col>
      </b-row>
    </b-container>
    <b-container style="text-align: center"
                 v-if="!viewableComps.length == 0 && isLoggedIn">
      <h3>einsehbare Kompositionen</h3>
      <b-row class="comprow">
        <b-col v-for="comp in viewableComps"
               :key="comp.id"
               class="compcol round">
          {{comp.name}} <br/>
          <span class="author">{{comp.owner.fullName}} <br /></span>
        </b-col>
      </b-row>
    </b-container>
    <b-container style="text-align: center"
                 v-if="!publicComps.length == 0">
      <h3 >öffentliche Kompositionen</h3>
      <b-row class="comprow">
        <b-col v-for="comp in publicComps"
               :key="comp.id"
               class="compcol round">
          {{comp.name}} <br/>
          <span class="author">{{comp.owner.fullName}} <br /></span>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>

export default {
  props: {
    isLoggedIn: Boolean,
    userId: Number
  },
  data() {
    return {
      editableComps: [],
      viewableComps: [],
      publicComps: []
    }
  },
  mounted() {
    this.axios({
      method: 'get',
      url: 'http://134.245.1.240:9061/composer-0.0.1-SNAPSHOT/compositions'
    }).then(response => {
      this.editableComps = response.data.editable
      this.viewableComps = response.data.viewable
      this.publicComps = response.data.publicComps
    })
    .catch(error => alert('Server nicht verfügbar'))
  }
}
</script>

<style>
h3 {
  text-align: left;
  font-weight: bold;
}
.author {
  position: absolute;
  margin-top: 1vh
}
.comprow{
  padding-left: 5%;
  margin-bottom: 5%;
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
  white-space:nowrap;
  text-overflow: ellipsis;
}
.edit {
  position: absolute;
  bottom: 6px;
  right: 6px;
}
</style>
