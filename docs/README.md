# ICU Website Using AstroJS Starlight

[![Built with Starlight](https://astro.badg.es/v2/built-with-starlight/tiny.svg)](https://starlight.astro.build)


## Developing Locally

### 🧞 Commands

All commands are run from the root of the project, from a terminal:

| Command                   | Action                                           |
| :------------------------ | :----------------------------------------------- |
| `npm install`             | Installs dependencies                            |
| `npm run dev`             | Starts local dev server at `localhost:4321`      |
| `npm run build`           | Build your production site to `./dist/`          |
| `npm run preview`         | Preview your build locally, before deploying     |
| `npm run astro ...`       | Run CLI commands like `astro add`, `astro check` |
| `npm run astro -- --help` | Get help using the Astro CLI                     |

### 👀 Want to learn more?

Check out [Starlight’s docs](https://starlight.astro.build/), read [the Astro documentation](https://docs.astro.build), or jump into the [Astro Discord server](https://astro.build/chat).

## Setup commands

The following commands were be run to setup AstroJS with Starlight
as well as additional plugins, etc. that are needed for ICU customizations to the Starlight theme:

1. Install [Starlight docs](https://github.com/withastro/starlight), a theme for Astro
    ```
    npm create astro@5.6.1 -- --template starlight
    ```
1. Install [Flexoki](https://github.com/delucis/starlight-theme-flexoki), a theme for Starlight
    ```
    npm install starlight-theme-flexoki@0.1.0
    ```
1. Install [Starlight Sidebar Topics Dropdown](https://github.com/trueberryless-org/starlight-sidebar-topics-dropdown),
which visually/mentally splits the site into separate sections,
toggled by a dropdown menu.
It works by installing [Starlight Sidebar Topics](https://github.com/HiDeoo/starlight-sidebar-topics)
and adding the dropdown functionality on top.
    ```
    npm i starlight-sidebar-topics-dropdown@0.5.1
    # v0.5.1 works with starlight-sidebar-topics@0.6.0 at least
    ```
1. Install [Starlight Links Validator](https://github.com/HiDeoo/starlight-links-validator) to enable automatic checking of internal links
    ```
    npm i starlight-links-validator@0.14.0
    ```
    Thereafter, production builds (ex: via `npm run build`) will automatically check the validity of internal links.

    Note: using version 0.14.0 gives you a report of which links are broken when running the production build. But later versions like 0.14.2 and 0.14.3 may not.

    Note: You may want/need to run `npm audit fix` to fix dependency problems in the npm installation.

    Note: if you configure the plugin to allow relative URLs, its behavior will then change to _ignore_ the validation of relative links. In this way, it appears that the plugin is opinionated on not fully supporting relative links.
1. Install [Astro Embed](https://github.com/delucis/astro-embed) to install Astro components for embedding multimedia (ex: videos) in pages in a lightweight way.
    ```
    npm i astro-embed@0.9.0
    ```
