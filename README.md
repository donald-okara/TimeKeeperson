### Module Graph

```mermaid
%%{
  init: {
    'theme': 'neutral'
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