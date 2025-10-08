import { createApp, reactive, provide } from 'vue'
import App from './App.vue'
import './styles.css'

const app = createApp({
  setup() {
    const store = reactive({
      sseOnline: false,
      lastEvent: null
    })
    provide('store', store)
  },
  render() {
    return h(App)
  }
})
import { h } from 'vue'
app.mount('#app')
