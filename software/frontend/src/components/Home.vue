<template>
  <div class="mainlayout round">
    <div>
      <MainLayout style="text-align: right; ">
        <NavBar @workspace="show('workspace')" @adminpanel="show('adminpanel')" @login="show('login')" />
      </MainLayout>
    </div>
    <div style="margin-bottom: 50px">
      <MainLayout v-if="msg == 'workspace'">
        <Workspace @openComp="openComp"/>
      </MainLayout>
      <MainLayout v-else-if="msg == 'adminpanel'">
        <AdminPanel />
      </MainLayout>
      <MainLayout v-else-if="msg == 'login'">
        <Login />
      </MainLayout>
      <MainLayout v-else-if="msg == 'editor'">
        <div style="overflow: hidden">
          <SidePanel style="float: left; width: 15%; border-right-style: solid; padding-right: 10px"/>
          <Editor :comp-id="id" style="float: right; width: 80%"/>
        </div>
      </MainLayout>
      <MainLayout v-else>
        That shouldn't have happened.
      </MainLayout>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar'
import AdminPanel from '@/components/AdminPanel'
import Workspace from '@/components/Workspace'
import MainLayout from '@/layouts/MainLayout'
import SidePanel from '@/components/SidePanel'
import Editor from '@/components/Editor'
import Login from '@/components/Login'

export default {
  data() {
    return {
      msg: 'workspace',
      id: 0
    }
  },
  components: {
    NavBar, AdminPanel, Workspace, MainLayout, SidePanel, Editor, Login
  },
  methods: {
    show(arg) {
      this.msg = arg
    },
    openComp(id) {
      this.msg = 'editor'
      this.id = id
    }
  }
}
</script>

<style>
  .navbar {
    margin-top: 6px;
    margin-right: 6px;
    text-align: right;
  }
  .mainlayout {
    max-width: 90%;
    margin: 50px auto;
    padding: 15px 30px;
    background: #cce6ff;
  }
  .round {
    border-radius: 10px;
  }
</style>
