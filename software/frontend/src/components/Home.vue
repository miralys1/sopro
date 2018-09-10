<template>
  <div class="mainlayout round">
    <div>
      <MainLayout style="text-align: right; ">
        <NavBar @workspace="show('workspace')" @adminpanel="show('adminpanel')" @login="show('login')" />
      </MainLayout>
    </div>
    <div style="margin-bottom: 10px">
      <MainLayout v-if="msg == 'workspace'">
        <Workspace @openComp="openComp"/>
      </MainLayout>
      <MainLayout v-else-if="msg == 'adminpanel'">
        <AdminPanel />
      </MainLayout>
      <MainLayout v-else-if="msg == 'login'">
        <Login />
      </MainLayout>
    </div>
    <div style="height: portrait">
    <MainLayout v-if="msg == 'editor'">
        <Editor/>
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
  .mainlayout {
    max-width: 100%;
    margin: 0px auto;
    padding: 0px 0px;
  }
  .round {
    border-radius: 4px;
  }
</style>
