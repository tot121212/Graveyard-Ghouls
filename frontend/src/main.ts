import { mount } from 'svelte';
import App from './App.svelte';
import './app.css';
import { activateStomp } from './stomp/client';

const app = mount(App, {
    target: document.getElementById('app')!,
});

activateStomp();

export default app;
