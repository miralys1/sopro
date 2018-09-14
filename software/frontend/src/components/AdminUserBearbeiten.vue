<template>
  <div class = "out">
    <input type="text" placeholder="Suche.." v-model="searchedUser" @keyup.enter="search">
    <br><br>
    <div class="list-group list-group-flush" style="overflow:scroll;">
    <button type="button" class="list-group-item list-group-item-action" style="display: inline-block" v-for="(user,index) in users" @click="onClick(index)">
      {{user.firstName}} {{user.lastName}}
    </button>
  </div>
  <br><br>
    <b-form @submit="onSubmit" @reset="onReset" v-if="show">
      <b-button type="submit" variant="primary" style="float:right;">Submit</b-button>
      <b-button type="reset" variant="danger" style="float:right;">Reset</b-button>
      <h4>Benutzer Information</h4>
      <b-form-group id="title"
                    label="Anrede:"
                    label-for="genTitle">
        <b-form-input id="genTitle"
                      type="text"
                      v-model="user.title"
                      required
                      placeholder="Enter a title">
        </b-form-input>
      </b-form-group>
      <b-form-group id="FName"
                    label="Vorname:"
                    label-for="genFName">
        <b-form-input id="genFName"
                      type="text"
                      v-model="user.firstName"
                      required
                      placeholder="Enter a firstname">
        </b-form-input>
      </b-form-group>
      <b-form-group id="LName"
                    label="Nachname:"
                    label-for="genLName">
        <b-form-input id="genLName"
                      type="text"
                      v-model="user.lastName"
                      required
                      placeholder="Enter a lastname">
        </b-form-input>
      </b-form-group>
      <b-form-group id="Email"
                    label="E-Mail Adresse:"
                    label-for="genEmail">
        <b-form-input id="genEmail"
                      type="text"
                      v-model="user.email"
                      required
                      placeholder="Enter an email adress">
        </b-form-input>
      </b-form-group>

      <b-form-group id="Admin"
                    label="Administrator:"
                    label-for="genAdmin">
        <b-form-select id="genAdmin"
                       :options="admin"
                       required
                       v-model="user.isAdmin">
        </b-form-select>
      </b-form-group>



    </b-form>
  </div>
</template>


<script>

export default {

data() {
  return {

  searchedUser: "",
  user: {
    id: "",
    firstName: "",
    lastName: "",
    email:"",
    title:"",
    isAdmin: false
  },
  backupUser: {
    id: "",
    firstName: "",
    lastName: "",
    email:"",
    title:"",
    isAdmin: false
  },

  users: [
     {
      id: "1",
      firstName: "Anna",
      lastName: "Müller",
      email:"Bla@bla",
      title:"Frau",
      isAdmin: true
    },
     {
      id: "2",
      firstName: "Ben",
      lastName: "Bär",
      email:"jj@kk",
      title:"Herr",
      isAdmin: false
    },
  ],

  admin: [
  { text: 'Nein', value: false },
  { text: 'Ja', value: true}
],
show: true
}
},

methods: {
  onSubmit (evt) {

      evt.preventDefault();

      alert(JSON.stringify(this.user));

      this.axios.post('http://134.245.1.240:9061/composer-0.0.1-SNAPSHOT/users', JSON.stringify(this.user))
               .then(function (response) { alert(response);})
               .catch(function (error) {alert(error);});

  },

  onReset (evt) {
    evt.preventDefault();
    /* Reset our form values */
     this.user = JSON.parse(JSON.stringify(this.backupUser));

    this.show = false;
    this.$nextTick(() => { this.show = true });
  },

  search (evt) {
   this.users = [];

   this.axios.get('http://134.245.1.240:9061/composer-0.0.1-SNAPSHOT/users')
            .then(response =>
            this.users = response.data
                )
            .catch(function (error) {
             alert("Fehler");
            console.log(error);
                });
 }



  },

  onClick(index) {
     var id = this.users[idex].id;
    //get request mit id und antwort auf this user und this backup user
    this.user = JSON.parse(JSON.stringify(this.users[index])) ;
    this.backupUser = JSON.parse(JSON.stringify(this.users[index])) ;
  }

}

</script>
