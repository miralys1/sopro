<template>
  <div class = "out">

    <input class="search" type="text" placeholder="Suche.." v-model="searchedUser" @keyup.enter="search">

    <br><br>
    <!-- dynamic generation of user list -->
    <div v-if="showList" class="list-group list-group-flush" style="overflow-y:scroll; max-height: 200px;">
    <button type="button" class="list-group-item list-group-item-action" style="display: inline-block; margin: auto auto; min-height: 10vh;" v-for="(user,index) in users" @click="onClick(index)">
    {{user.title}} {{user.firstName}} {{user.lastName}}
    </button>
  </div>
  <br><br>
    <b-form @submit="onSubmit" @reset="onReset" v-if="showForm">
   <!-- delete button with confirmation dialogue -->
      <b-button v-b-modal.Prevent variant="danger" style="float:left;" >Löschen</b-button>
        <b-modal id="Prevent"
              cancel-title = "Abbrechen"
              ok-variant = "danger"
              ok-title =  "Löschen"
              @ok="onDelete"
              title = "Bestätigen">
              Sind Sie sicher, dass Sie löschen wollen?
        </b-modal>



      <b-button type="submit" variant="primary" style="float:right ;margin-left: 0.5vw;">Speichern</b-button>
      <b-button type="reset" variant="warning" style="float:right;">Reset</b-button>
      <br><br>
      <h4>Benutzer Information</h4>
      <b-form-group id="title"
                    label="Anrede:"
                    label-for="genTitle">
        <b-form-input id="genTitle"
                      type="text"
                      v-model="user.title">
        </b-form-input>
      </b-form-group>
      <b-form-group id="FName"
                    label="Vorname:"
                    label-for="genFName">
        <b-form-input id="genFName"
                      type="text"
                      v-model="user.firstName"
                      required>
        </b-form-input>
      </b-form-group>
      <b-form-group id="LName"
                    label="Nachname:"
                    label-for="genLName">
        <b-form-input id="genLName"
                      type="text"
                      v-model="user.lastName"
                      required>
        </b-form-input>
      </b-form-group>
      <b-form-group id="Email"
                    label="E-Mail Adresse:"
                    label-for="genEmail">
        <b-form-input id="genEmail"
                      type="text"
                      v-model="user.email"
                      required>
        </b-form-input>
      </b-form-group>

      <!-- an admin cannot change his/her own admin status (to guarantee that there is always at least one user)-->
      <b-form-group id="Admin"
                    label="Administrator:"
                    label-for="genAdmin">
        <b-form-select id="genAdmin"
                       :options="admin"
                       :disabled = "userSeesHimself"
                       required
                       v-model="user.admin">
        </b-form-select>
      </b-form-group>



    </b-form>
  </div>
</template>


<script>

export default {
  props: ["loggedInUser"],

  watch: {
    searchedUser: function() {
      if(this.searchedUser == ""){
        this.showForm = false;
        this.showList = false;
      }
    }
      },

data() {
  return {
  userSeesHimself: false,
  searchedUser: "",
  user: {
    id: "",
    firstName: "",
    lastName: "",
    email:"",
    title:"",
    admin: false
  },
  //to reset without changes
  backupUser: {
    id: "",
    firstName: "",
    lastName: "",
    email:"",
    title:"",
    admin: false
  },

  users: [],

  admin: [
  { text: 'Nein', value: false },
  { text: 'Ja', value: true}
],
showForm: false,
showList: false,
}
},

methods: {

  onDelete(){

    //preventing admin from deleting him/herselfs
    if(!this.userSeesHimself) {
    this.axios.delete('/users/'+ this.user.id)
             .then(response => {
             alert("User wurde gelöscht");
             this.showForm= false;
           this.search();}
                 )
             .catch(function (error) {
              alert("Fehler beim Löschen, Dienst wurde nicht gelöscht.");
                 });
    } else {
      alert("Sie versuchen sich selbst zu löschen, dies ist nur von einem anderen Administrator-Konto aus möglich.");
    }

  },

  onSubmit (evt) {

      evt.preventDefault();

      this.axios({
        url: '/users/'+this.user.id,
        method: 'put',
        data: this.user,
        headers: {
          "Content-Type": "application/json"
        }
      }).then(response => { alert("Erfolgreich geändert.");this.showForm= false; this.search();})
        .catch(error => {alert(error);});

  },

  onReset (evt) {
    evt.preventDefault();
    /* Reset our form values */
     this.user = JSON.parse(JSON.stringify(this.backupUser));

    this.showForm = false;
    this.$nextTick(() => { this.showForm = true });
  },

  search () {
   this.showList = true;
   this.showForm = false;

 //after entering a search string the server has to select fitting users
   this.axios.get('/users?search='+ this.searchedUser)
            .then(response =>
            this.users = response.data
                )
            .catch(function (error) {
             alert("Fehler");
            console.log(error);
                });


  },

  onClick(index) {
//if a user is selected, detailed information are requested from the server
     var id = this.users[index].id;

     this.axios.get('/users/' + id)
              .then(response => {
              this.user = response.data;
              this.backupUser = JSON.parse(JSON.stringify(this.user)) ;
              this.showForm = true;
              this.userSeesHimself = (this.user.id == this.loggedInUser.id);
            }
                  )
              .catch(function (error) {
               alert("Die genauen Userdaten konnten nicht vom Server abgefragt werden");
                  });
  }

}
}
</script>

<style>
.search {
    background: transparent;
    border: none;
    border-bottom: 2px solid #000000;
}
</style>
