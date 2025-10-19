import { createApp, provide, h } from 'vue'
import App from './App.vue'
import './styles.css'
import { store } from './store'

const app = createApp({
  setup() {
    provide('store', store)
  },
  render() {
    return h(App)
  }
})
app.mount('#app')
