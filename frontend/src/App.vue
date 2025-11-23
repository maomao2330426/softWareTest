<template>
  <div id="app">
    <el-container style="height: 100vh">
      <el-header style="background-color: #409EFF; color: white; display: flex; align-items: center; justify-content: space-between;">
        <h2>智能客服工单管理系统</h2>
        <el-menu mode="horizontal" :ellipsis="false" background-color="#409EFF" text-color="#fff" active-text-color="#ffd04b" router>
          <el-menu-item index="/">工单列表</el-menu-item>
          <el-menu-item index="/create">创建工单</el-menu-item>
          <el-menu-item index="/admin">管理员</el-menu-item>
          <el-menu-item index="/cs">客服工作台</el-menu-item>
        </el-menu>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

#app {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
}

.el-header {
  padding: 0 20px;
}

.el-main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>
