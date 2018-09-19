<template>
  <div class="wideform form-group">

                     <!-- vunchecked-value="false" -->
                     <!-- value="true" -->
  <b-form-group
      id="editorsettings"
      label="Settings"
      label-for="input1">
    <b-form-checkbox id="origincheckbox"
                     v-model="showOrigin"
                     >
      Show Origin
    </b-form-checkbox>
  </b-form-group>

  <b-form-group
      v-if="owner"
      id="editorsettings"
      @submit="addUser"
      label="Permissions"
      label-for="input1">
      <b-form-input v-model="useremail"
                    type="email"
                    required
                    class="emailinput"
                    placeholder="Enter email of user to add">
      </b-form-input>
      <b-button size="sm" variant="success" @click="addUser">
        <v-icon name="plus"/>
      </b-button>
      <div>
        <ul class="list-group">
          <li v-for="user in editors"
              class="list-group-item">
            Cras justo odio
          </li>
          <li v-for="user in viewers"
              class="list-group-item">
          </li>
        </ul>
      </div>
  </b-form-group>
  </div>
</template>

<script>
export default {
    props: {
        compId: Number,
        owner: Boolean
    },
    computed: {
        config () {
            return {
               showOrigin: this.showOrigin
            }
        }
    },
    data () {
        return {
            useremail: '',
            editors: [],
            viewer: [],
            showOrigin: false
        }
    },
    methods: {
        addUser () {
            alert('added new user')
            this.axios.post('/compositions/' + this.compId + '/users')
                .then(response => {
                    console.log(response)
                })
                .catch(error => console.log("User doesn't' exist"))

            if(this.viewer.find(e => !(e===this.useremail))) this.viewers.push(useremail)
        },
    },
    watch: {
        config () {
            console.log('config changed');
            console.log(this.config);
            this.$emit("optionsChanged", this.config)
        }
    },
    mounted () {
        this.axios.get('/compositions/' + this.compId + '/users')
            .then(response => {
                this.editors = response.data.editors;
                this.viewers = response.data.viewers;
            })
            .catch(error => console.log(error))
    }
}
</script>

<style scoped>
  .wideform {
    margin-left: 20px;
    margin-right: 20px;
  }

  .emailinput {
    width: 250px;
  }
</style>
