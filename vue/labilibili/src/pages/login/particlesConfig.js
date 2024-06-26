export const particlesConfig = {
  fpsLimit: 50,
  background: {
    color:{
      value: '#98beee' // #0d42a9
    }
  },
  interactivity: {
      events: {
          onClick: {
              enable: true,
              mode: "push",
          },
          onHover: {
              enable: true,
              mode: "grab",
          },
          resize: true,
      },
      modes: {
          bubble: {
              distance: 400,
              duration: 2,
              opacity: 0.6,
              size: 10,
          },
          push: {
              quantity: 4,
          },
          repulse: {
              distance: 200,
              duration: 0.4,
          },
      },
  },
  particles: {
      color: {
          value: "#ffffff",
      },
      links: {
          color: "#ffffff",
          distance: 150,
          enable: true,
          opacity: 0.5,
          width: 1,
      },
      collisions: {
          enable: true,
      },
      move: {
          direction: "none",
          enable: true,
          outMode: "bounce",
          random: false,
          speed: 2,
          straight: false,
      },
      number: {
          density: {
              enable: true,
              value_area: 800,
          },
          value: 80,
      },
      opacity: {
          value: 0.5,
      },
      shape: {
          type: "circle",
      },
      size: {
          random: true,
          value: 3,
      },
  },
  detectRetina: true,
}