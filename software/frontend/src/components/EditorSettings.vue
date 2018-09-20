<template>
  <div class="wideform form-group">
  <b-form-group
      id="editorsettings"
      label="Einstellungen"
      label-for="input1">
    <b-form-checkbox id="origincheckbox"
                     v-model="showOrigin"
                     >
      Ursprung anzeigen
    </b-form-checkbox>
  </b-form-group>

  <b-form-group
      v-if="owner"
      id="editorsettings"
      @submit="addUser"
      label="Permissions"
      label-for="input1">
      <b-form-checkbox id="publicButton"
                      v-model="isPublic"
                      >
        Public
      </b-form-checkbox>
      <div class="emailinput">
        <b-input-group prepend="Email" class="emailinput">
            <b-form-input v-model="useremail"
                            type="email"
                            required
                            placeholder="Enter email of user to add">
            </b-form-input>
            <b-input-group-append>
                <b-btn variant="success" @click="addUser">
                    <v-icon name="plus" />
                </b-btn>
            </b-input-group-append>
        </b-input-group>
      </div>
      <div>
          <ul class="list-group">
              <li v-for="user in users"
                  class="list-group-item">
                    <b-form-checkbox :id="user.id + '-edit'"
                                     :checked="{id: user.id, permissions: user.canEdit ? 'editor' : 'viewer'}"
                                     @change="setPermissions"
                                     :value="{id: user.id, permissions: 'editor'}"
                                     :unchecked-value="{id: user.id, permissions: 'viewer'}"
                                    >
                    edit
                    </b-form-checkbox>
                    {{ user.name }}
                    <div  @click="deleteUser(user.id)" type="display:inline-block" >
                        <v-icon name="minus-square" scale="1" color="red"/>
                    </div>
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
               showOrigin: this.showOrigin,
               isPublic: this.isPublic
            }
        }
    },
    data () {
        return {
            useremail: '',
            users: [],
            showOrigin: true,
            isPublic: false
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
        deleteUser(id) {
            this.axios({
                    method: 'delete',
                    url: '/compositions/' + this.compId + '/users/' + id,
                })
                .then(response => {
                    console.log('user deleted')
                    this.users = this.users.filter(u => u.id!==id)
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
                this.axios.get('/compositions/' + this.compId + '/public')
                .then(response => {
                    this.isPublic = response.data
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
        },
        isPublic () {
            this.axios({
                    method: 'put',
                    url: '/compositions/' + this.compId + '/public',
                    headers: {"Content-Type": "application/json" },
                    data: this.isPublic
                })
                .then(response => {
                    console.log('made ' + (this.isPublic ? 'public' : 'private'))
                })
                .catch(error => alert( error + " Maybe User doesn't' exist"))
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
  width: 300px;
}

.emailinput {
  width: 300px;
  margin-top: 10px;
  margin-bottom: 10px;
}
</style>
