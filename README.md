# spar.bz

_now featuring clojurescript!_

#### stack

* [reagent](https://github.com/reagent-project/reagent)
* [figwheel](https://github.com/bhauman/lein-figwheel)
* [garden](https://github.com/noprompt/garden)
* [less](https://github.com/montoux/lein-less)
* [petrol](https://github.com/krisajenkins/petrol)
* [secretary](https://github.com/gf3/secretary)

_below is taken from [reagent-figwheel](https://github.com/gadfly361/reagent-figwheel)_

## Development Mode

### cljs-devtools

To enable:

1. Open Chrome's DevTools,`Ctrl-Shift-i`
2. Open "Settings", `F1`
3. Check "Enable custom formatters" under the "Console" section
4. close and re-open DevTools

### Compile css (garden):

Compile css file once.

```
lein garden once
```

Automatically recompile css file on change.

```
lein garden auto
```

### Compile css (less):

Compile css file once.

```
lein less once
```

Automatically recompile css file on change.

```
lein less auto
```

### Run application:

```
lein clean
lein figwheel dev
```

Figwheel will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:3449](http://localhost:3449).

### Run tests:

```
lein clean
lein doo phantom test once
```

The above command assumes that you have [phantomjs](https://www.npmjs.com/package/phantomjs) installed. However, please note that [doo](https://github.com/bensu/doo) can be configured to run cljs.test in many other JS environments (chrome, ie, safari, opera, slimer, node, rhino, or nashorn).

## Production Build

```
lein clean
lein cljsbuild once min
```
