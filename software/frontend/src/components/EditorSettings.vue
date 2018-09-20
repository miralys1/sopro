<template>
  <div class="wideform form-group">
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
      <div>
        <b-form-input v-model="useremail"
                        type="email"
                        required
                        class="emailinput"
                        placeholder="Enter email of user to add">
        </b-form-input>
        <b-button id="addButton" size="sm" variant="success" @click="addUser">
            <v-icon name="plus"/>
        </b-button>
        </div>
        <div>
            <ul class="list-group">
                <li v-for="user in users"
                    class="list-group-item">

                      <b-form-checkbox :id="user.id + '-edit'"
                                       @change="setPermissions"
                                       :value="{id: user.id, permissions: 'viewer'}"
                                       :unchecked-value="{id: user.id, permissions: 'editor'}"
                                      >
                      edit
                      </b-form-checkbox>
                    {{ user.name }}
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
            users: [],
            showOrigin: false
        }
    },
    methods: {
        addUser () {
            this.axios({
                    method: 'post',
                    url: '/compositions/' + this.compId + '/users/' + this.useremail,
                    headers: {"Content-Type": "application/json" },
                    data: 'viewer'
                })
                .then(response => {
                    if(this.users.filter(e => e.id===response.data.id).length == 0) {
                        console.log('pushed new email')
                        this.getPermissions()
                    }
                })
                .catch(error => alert( error + " Maybe User doesn't' exist"))
        },
        getPermissions () {
            if(this.owner) {
                this.axios.get('/compositions/' + this.compId + '/users')
                .then(response => {
                    this.users = response.data.viewers.map(u => ({id: u.id, name: u.fullName, canEdit: false}))
                        .concat(response.data.editors.map(u => ({id: u.id, name: u.fullName, canEdit: true})))
                })
                .catch(error => console.log(error))
            }
        },
        setPermissions (perms) {
            this.axios({
                    method: 'put',
                    url: '/compositions/' + this.compId + '/users/' + perms.id,
                    headers: {"Content-Type": "application/json" },
                    data: perms.permissions
                })
                .then(response => {
                    if(this.users.filter(e => e.id===response.data.id).length == 0) {
                        console.log('pushed new email')
                        this.getPermissions()
                    }
                })
                .catch(error => alert( error + " Maybe User doesn't' exist"))
        }
    },
    watch: {
        config () {
            console.log('config changed');
            console.log(this.config);
            this.$emit("optionsChanged", this.config)
        }
    },
    mounted () {
        this.getPermissions()
    }
}
</script>

<style scoped>
  .wideform {
    margin-left: 20px;
    margin-right: 20px;
  }

  .addButton {
    position: absolute;
    top: 0px;
    right: 10px;
  }

  .emailinput {
    width: 250px;
  }
</style>
