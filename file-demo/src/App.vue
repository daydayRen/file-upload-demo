<template>
  <div class="appmain">

<!--     <img alt="Vue logo" src="./assets/logo.png">-->
    <menuvue></menuvue>
  </div>
</template>

<script>

import Menuvue from "@/components/menuvue.vue";

export default {
  name: 'App',
  components: {Menuvue},
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
