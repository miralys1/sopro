<template>
  <div v-if="show">
    <b-jumbotron class="jumbo" v-if="!user.loggedIn && publicComps.length == 0" header="Keine öffentlichen Kompositionen verfügbar :(" lead="Registrieren Sie sich jetzt, um Kompositionen zu erstellen">
      <b-btn variant="primary" :to="'Login'">Hier registrieren</b-btn>
    </b-jumbotron>
    <div v-else>
    <b-jumbotron class="jumbo" v-if="user.loggedIn && publicComps.length == 0 && ownComps.length == 0 && editableComps.length == 0 && viewableComps.length == 0" header="Keine Komposition verfügbar :(" lead="Erstellen Sie jetzt ihre erste Komposition" >
      <b-input-group style="width: 30vw">
        <b-form-input v-model="name" type="text" placeholder="Kompositionsname" />
        <b-input-group-append>
          <b-btn @click="createComp" variant="success">erstellen</b-btn>
        </b-input-group-append>
      </b-input-group>
    </b-jumbotron>
    <div v-else class="mainlayout" style="overflow: hidden">
    <b-input-group style="float: right; width: 15vw" v-if="user.loggedIn">
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
               @click.self="open(comp.id)">
          <span class="title" @click="open(comp.id)">{{comp.name}}</span>
          <b-btn class="deletebtn" variant="danger" @click="uwudelete(comp.id)">
            <v-icon name="trash" />
          </b-btn>
        </b-col>
      </b-row>
    </b-container>
    <b-container v-if="!editableComps.length == 0 && user.loggedIn">
      <h3>bearbeitbare Kompositionen</h3>
      <b-row class="comprow">
        <b-col v-for="comp in editableComps"
               :key="comp.id"
               class="compcol round link"
               cols="2"
               @click="open(comp.id)">

              <span class="title">{{comp.name}} <br/></span>
              <span class="author">{{comp.owner.fullName}} <br /></span>
        </b-col>
      </b-row>
    </b-container>
    <b-container v-if="!viewableComps.length == 0 && user.loggedIn">
      <h3>einsehbare Kompositionen</h3>
      <b-row class="comprow">
        <b-col v-for="comp in viewableComps"
               :key="comp.id"
               class="compcol round link"
               cols="2"
               @click="open(comp.id)">
              <span class="title">{{comp.name}} <br/></span>
              <span class="author">{{comp.owner.fullName}} <br /></span>
        </b-col>
      </b-row>
    </b-container>
    <b-container v-if="!publicComps.length == 0">
      <h3>&ouml;ffentliche Kompositionen</h3>
      <b-row class="comprow">
        <b-col v-for="comp in publicComps"
               :key="comp.id"
               class="compcol round link"
               cols="2"
               @click="open(comp.id)">
              <span class="title">{{comp.name}} <br/></span>
              <span class="author">{{comp.owner.fullName}} <br /></span>
        </b-col>
      </b-row>
    </b-container>
  </div>
</div>
  </div>
</template>

<script>

export default {
  props: {
    user: Object
  },
  data() {
    return {
      show: false,
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
      })
      .catch(() => this.$refs.createCompFailModal.show())
    },
    open(id) {
      this.$router.push({
              name: 'Editor',
              params: { compId: id }
            })
    },
    uwudelete(id) {
      this.axios({
        method: 'delete',
        url: '/compositions/' + id
      }).then(res => {
        alert('Kompowosition ' + id + ' uwurde gelöscht');
        this.ownComps = this.ownComps.filter(comp => comp.id !== id);
      }).catch(err => alert('Oopsie someeeething went wrong uwu'))
    },
    getComps() {
      this.show = false
      this.axios({
        url: '/compositions',
        method: 'get'
      }).then(response => {
        this.ownComps = response.data.owns
        this.editableComps = response.data.editable
        this.viewableComps = response.data.viewable
        this.publicComps = response.data.publicComps
        this.show = true
      })
      .catch(error => {
        alert('Server nicht verfügbar');
        this.show = true
      })

    },
    hideCreateCompFailModal() {
      this.$refs.createCompFailModal.hide()
    }
  },
  watch: {
    user: {
      handler() {
        this.getComps();
      },
      deep: true
    }
  },
  mounted() {
    this.getComps()
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
.deletebtn {
  position: absolute;
  bottom: 5%;
  right: 5%;
}
.link {
  color: black;
}
.link:hover {
  cursor: pointer;
  color: black;
  color: blue;
  text-decoration: none;
}
.jumbo {
  margin: 20vh auto;
  width: 65vw;
}
</style>
