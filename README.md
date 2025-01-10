### Module Graph

```mermaid
%%{
  init: {
    'theme': 'base',
    'themeVariables': {"primaryTextColor":"#FFFFFF","primaryColor":"#5A4F7C","lineColor":"#F5A623"}
  }
}%%

graph LR
  :app-navigation --> :core-domain
  :app-navigation --> :app-home
  :app-navigation --> :feature-timer
  :app-home --> :feature-timer
  :app-home --> :core-datasource
  :app --> :app-navigation
  :feature-timer --> :core-datasource
  :feature-timer --> :core-domain
```