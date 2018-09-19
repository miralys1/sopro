<template>
  <div class="wideform form-group">

  <b-form-group
      id="editorsettings"
      label="Settings"
      label-for="input1">
    <b-form-checkbox id="origincheckbox"
                     value="accepted"
                     unchecked-value="not_accepted">
      Show Origin
    </b-form-checkbox>
  </b-form-group>

  <b-form-group
      v-if="owner"
      id="editorsettings"
      @submit="onSubmit"
      @reset="onReset"
      label="Permissions"
      label-for="input1">
    <b-form-input v-model="useremail"
                  type="text"
                  class="emailinput"
                  @submit="addUser"
                  placeholder="Enter email of user to add"></b-form-input>
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
    data () {
        return {
            useremail: '',
            editors: [],
            viewer: [],

            options: {
               showOrigin: false
            }
        }
    },
    methods: {
        addUser () {
            this.axios.put('/compositions/' + this.compId + '/users')
                .then(response => {
                    console.log(response)
                })
                .catch(error => console.log("User doesn't' exist"))

            if(this.viewer.find(e => !(e===this.useremail))) this.viewers.push(useremail)
        },
    },
    watch: {
        options () {
            this.$emit("optionsChanged", this.options)
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
