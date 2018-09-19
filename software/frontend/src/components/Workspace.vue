<template>
  <div class="mainlayout" style="overflow: hidden">
    <b-input-group style="float: right; width: 15vw">
      <b-form-input v-model="name" type="text" placeholder="Kompositionsname" />
      <b-input-group-append>
        <b-btn @click="createComp" variant="success">erstellen</b-btn>
      </b-input-group-append>
    </b-input-group>
    <b-container v-if="!ownComps.length == 0 && user.loggedIn">
      <h3>eigene Kompositionen</h3>
      <b-row class="comprow">
        <b-col v-for="comp in ownComps"
               :key="comp.id"
               class="compcol round link"
               cols="2"
               @click="open">
          <!-- <router-link class="link" :to="{
            name: 'Editor',
            params: { compId: comp.id }
          }"> -->
            <div style="width: 200px; height: 150px">
              <span class="title">{{comp.name}} <br/></span>
            </div>
          <!-- </router-link> -->
        </b-col>
      </b-row>
    </b-container>
    <b-container v-if="!editableComps.length == 0 && user.loggedIn">
      <h3>bearbeitbare Kompositionen</h3>
      <b-row class="comprow">
        <b-col v-for="comp in editableComps"
               :key="comp.id"
               class="compcol round"
               cols="2">
          <router-link class="link" :to="{
                 name: 'Editor',
                 params: { compId: comp.id }
               }">
            <div style="width: 200px; height: 150px">
              <span class="title">{{comp.name}} <br/></span>
              <span class="author">{{comp.owner.fullName}} <br /></span>
            </div>
          </router-link>
        </b-col>
      </b-row>
    </b-container>
    <b-container v-if="!viewableComps.length == 0 && user.loggedIn">
      <h3>einsehbare Kompositionen</h3>
      <b-row class="comprow">
        <b-col v-for="comp in viewableComps"
               :key="comp.id"
               class="compcol round"
               cols="2">
          <router-link class="link" :to="{
                  name: 'Editor',
                  params: { compId: comp.id }
                }">
            <div style="width: 200px; height: 150px">
              <span class="title">{{comp.name}} <br/></span>
              <span class="author">{{comp.owner.fullName}} <br /></span>
            </div>
          </router-link>
        </b-col>
      </b-row>
    </b-container>
    <b-container v-if="!publicComps.length == 0">
      <h3>&ouml;ffentliche Kompositionen</h3>
      <b-row class="comprow">
        <b-col v-for="comp in publicComps"
               :key="comp.id"
               class="compcol round"
               cols="2">
          <router-link class="link" :to="{
                  name: 'Editor',
                  params: { compId: comp.id }
                }">
            <div style="width: 200px; height: 150px">
              <span class="title">{{comp.name}} <br/></span>
              <span class="author">{{comp.owner.fullName}} <br /></span>
            </div>
          </router-link>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>

export default {
  props: {
    user: Object
  },
  data() {
    return {
      name: '',
      ownComps: [],
      editableComps: [],
      viewableComps: [],
      publicComps: []
    }
  },
  methods: {
    createComp() {
      this.axios({
        url: '/compositions',
        method: 'post',
        data: this.name,
        headers: {
          'Content-Type': 'application/json'
        }
      }).then(res => {
        this.$router.push('/editor/' + res.data)
        alert('Komposition erfolgreich erstellt')
      })
      .catch(err => alert('Komposition erstellen fehlgeschlagen'))
    }
  },
  mounted() {
    this.axios({
      url: '/compositions',
      method: 'get'
    }).then(response => {
      this.ownComps = response.data.owns
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
  text-align: left;
  font-size: 15px;
  border: 1px solid black;
  height: 130px;
  overflow: hidden;
  position: relative;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.compcol:hover {
  border: 2px solid black;
}
.title {
  font-size: 20px;
  font-weight: bold
}
.link {
  color: black;
}
.link:hover {
  color: black;
  color: blue;
  text-decoration: none;
}
</style>
