<template>
  <div v-if="show">
    <!-- message when no public comps exist and user is not logged in -->
    <b-jumbotron class="jumbo" v-if="!user.loggedIn && publicComps.length == 0" header="Keine öffentlichen Kompositionen verfügbar :(" lead="Registrieren Sie sich jetzt, um Kompositionen zu erstellen">
      <b-btn variant="primary" :to="'Login'">Hier registrieren</b-btn>
    </b-jumbotron>
    <div v-else>
      <!-- message when logged in but no composition exists that user is allowed to see -->
    <b-jumbotron class="jumbo" v-if="user.loggedIn && publicComps.length == 0 && ownComps.length == 0 && editableComps.length == 0 && viewableComps.length == 0" header="Keine Komposition verfügbar :(" lead="Erstellen Sie jetzt ihre erste Komposition" >
      <b-input-group style="width: 30vw">
        <b-form-input v-model="name" type="text" placeholder="Kompositionsname" />
        <b-input-group-append>
          <b-btn @click="createComp" variant="success">erstellen</b-btn>
        </b-input-group-append>
      </b-input-group>
    </b-jumbotron>
    <!-- composition list -->
    <div v-else class="mainlayout" style="overflow: hidden">

      <!-- create new composition field+button -->
    <b-input-group style="float: right; width: 15vw" v-if="user.loggedIn">
      <b-form-input v-model="name" type="text" placeholder="Kompositionsname" />
      <b-input-group-append>
        <b-btn @click="createComp" variant="success">erstellen</b-btn>
      </b-input-group-append>
    </b-input-group>

    <!-- own compositions -->
    <b-container v-if="!ownComps.length == 0 && user.loggedIn">
      <h3>eigene Kompositionen</h3>
      <b-row class="comprow">
        <b-col v-for="comp in ownComps"
               :key="comp.id"
               class="compcol round link"
               cols="2"
               @click.self="open(comp.id)">
          <span class="title" @click="open(comp.id)">{{comp.name}}</span>
          <!-- delete button -->
          <b-btn class="deletebtn" variant="danger" @click="uwudelete(comp.id)">
            <v-icon name="trash" />
          </b-btn>
        </b-col>
      </b-row>
    </b-container>

    <!-- editable compositions -->
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

    <!-- viewable compositions -->
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

    <!-- public compositions -->
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
    // when create composition button is pressed
    createComp() {
      // request to server
      this.axios({
        url: '/compositions',
        method: 'post',
        data: this.name,
        headers: {
          'Content-Type': 'application/json'
        }
      }).then(res => {
        // if successful go to editor with new composition
        this.$router.push('/editor/' + res.data)
      })
      // if unsuccessful show failure alert
      .catch(() => this.$refs.createCompFailModal.show())
    },

    // when composition is clicked
    open(id) {
      // forward to editor with specific composition
      this.$router.push({
              name: 'Editor',
              params: { compId: id }
            })
    },
    // when delete button of a composition is pressed
    uwudelete(id) {
      // server request
      this.axios({
        method: 'delete',
        url: '/compositions/' + id
      }).then(res => {
        // when successful show alert
        alert('Komposition ' + id + ' wurde gelöscht');
        // delete composition locally
        this.ownComps = this.ownComps.filter(comp => comp.id !== id);
        // if unsuccessful show failure alert
      }).catch(err => alert('Etwas ist schief gegangen'))
    },
    // function to get all compositions
    getComps() {
      // boolean that prevents anything from rendering until request is done
      this.show = false
      // request
      this.axios({
        url: '/compositions',
        method: 'get'
      }).then(response => {
        // if successful save data
        this.ownComps = response.data.owns
        this.editableComps = response.data.editable
        this.viewableComps = response.data.viewable
        this.publicComps = response.data.publicComps
        this.show = true
      })
      .catch(error => {
        // if not show alert
        alert('Server nicht verfügbar');
        this.show = true
      })

    },
    hideCreateCompFailModal() {
      this.$refs.createCompFailModal.hide()
    }
  },
  watch: {
    // refreshes composition list every time user is logging out or in
    user: {
      handler() {
        this.getComps();
      },
      deep: true
    }
  },
  // when mounting app get comp list
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
