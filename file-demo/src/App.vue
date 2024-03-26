<template>
  <div class="appmain">

<!--     <img alt="Vue logo" src="./assets/logo.png">-->
    <menuvue></menuvue>
  </div>
</template>

<script>

import Menuvue from "@/components/menuvue.vue";

/*解决页面报错ERROR ResizeObserver loop completed with undelivered notifications*/
const debounce = (fn, delay) => {
  let timer = null;
  return function () {
    let context = this;
    let args = arguments;
    clearTimeout(timer);
    timer = setTimeout(function () {
      fn.apply(context, args);

    }, delay);
  }
}

const _ResizeObserver = window.ResizeObserver;
window.ResizeObserver = class ResizeObserver extends _ResizeObserver {
  constructor(callback) {
    callback = debounce(callback, 16);
    super(callback);
  }
}


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
