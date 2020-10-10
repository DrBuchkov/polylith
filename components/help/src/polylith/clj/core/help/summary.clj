(ns polylith.clj.core.help.summary
  (:require [polylith.clj.core.help.shared :as s]
            [polylith.clj.core.version.interface :as version]
            [polylith.clj.core.util.interface.color :as color]))

(defn help-text [cm]
  (str
    "  Poly " version/version " (" version/date ") - " (color/blue cm "https://github.com/polyfy/polylith\n")
    "\n"
    "  poly " (s/key "CMD" cm) " [" (s/key "ARGS" cm) "] - where " (s/key "CMD" cm) " [" (s/key "ARGS" cm) "] are:\n"
    "\n"
    "    check                   Checks if the workspace is valid.\n"
    "    create " (s/key "E" cm) " name:" (s/key "N" cm) " [" (s/key "ARG" cm) "]   Creates a component, base, environment or workspace.\n"
    "    deps [env:" (s/key "E" cm) "] [brick:" (s/key "B" cm) "]  Shows dependencies.\n"
    "    diff                    Shows changed files since last stable point in time.\n"
    "    help [" (s/key "C" cm) "] [" (s/key "ARG" cm) "]          Shows this help or help for specified command.\n"
    "    info [" (s/key "ARGS" cm) "]             Shows a workspace overview and checks if it's valid.\n"
    "    libs                    Shows all libraries in the workspace.\n"
    "    test [" (s/key "ARGS" cm) "]             Runs tests.\n"
    "    ws [get:" (s/key "X" cm) "]              Shows the workspace as data.\n"
    "\n"
    "  If " (s/key "ws-dir:PATH" cm) " is passed in as an argument, where " (s/key "PATH" cm) " is a relative\n"
    "  or absolute path, then the command is executed from that directory.\n"
    "  This works for all commands except 'create' and 'test'.\n"
    "\n"
    "  If " (s/key "::" cm) " is passed in, then ws-dir is set to the first parent directory\n"
    "  (or current) that contains a 'deps.edn' workspace config file. The exception\n"
    "  is the 'test' command that has to be executed from the workspace root.\n"
    "\n"
    "  If " (s/key "ws-file:FILE" cm) " is passed in, then the workspace will be populated with the\n"
    "  content from that file. All commands except 'create' and 'test'\n"
    "  can be executed with this parameter set. The " (s/key "FILE" cm) " is created by executing the\n"
    "  'ws' command, e.g.: 'poly ws out:ws.edn'.\n"
    "\n"
    "  If " (s/key "since:SINCE" cm) " is passed in as an argument, the last stable point in time\n"
    "  will be set depending on the value of " (s/key "SINCE" cm) " (or the first commit if no match\n"
    "  was found):\n"
    "    stable          -> the latest tag that matches stable-*, defined by\n"
    "                       " (s/key ":stable-tag-pattern" cm) " in ./deps.edn.\n"
    "    build           -> the latest tag that matches v[0-9]*, defined by\n"
    "                       " (s/key ":release-tag-pattern" cm) " in ./deps.edn.\n"
    "    previous-build  -> the latest tag that matches v[0-9]*,\n"
    "                       defined by " (s/key ":release-tag-pattern" cm) " in ./deps.edn.\n"
    "\n"
    "  The color mode can be overridden by passing in e.g. " (s/key "color-mode:none" cm) "\n"
    "  (valid values are: " (s/key "none" cm) ", " (s/key "light" cm) ", " (s/key "dark" cm) ") which is otherwise configured in\n"
    "  ~/.polylith/config.edn.\n"
    "\n"
    "  Example:\n"
    "    poly check\n"
    "    poly create c name:user\n"
    "    poly create c name:admin interface:user\n"
    "    poly create b name:mybase\n"
    "    poly create e name:myenv\n"
    "    poly create w name:myws top-ns:com.my.company\n"
    "    poly deps\n"
    "    poly deps env:myenv\n"
    "    poly deps brick:mybrick\n"
    "    poly deps env:myenv brick:mybrick\n"
    "    poly diff\n"
    "    poly help\n"
    "    poly help info\n"
    "    poly help create\n"
    "    poly help create c\n"
    "    poly help create b\n"
    "    poly help create e\n"
    "    poly help create w\n"
    "    poly help deps\n"
    "    poly help deps :env\n"
    "    poly help deps :brick\n"
    "    poly help deps :env :brick\n"
    "    poly info\n"
    "    poly info :loc\n"
    "    poly info since:release\n"
    "    poly info since:previous-release\n"
    "    poly info env:myenv\n"
    "    poly info env:myenv:another-env\n"
    "    poly info :env\n"
    "    poly info :dev\n"
    "    poly info :env :dev\n"
    "    poly info :all\n"
    "    poly info :all-bricks\n"
    "    poly info ::\n"
    "    poly info color-mode:none\n"
    "    poly info ws-dir:another-ws\n"
    "    poly info ws-file:ws.edn\n"
    "    poly libs\n"
    "    poly test\n"
    "    poly test env:myenv\n"
    "    poly test env:myenv:another-env\n"
    "    poly test :env\n"
    "    poly test :dev\n"
    "    poly test :env :dev\n"
    "    poly test :all\n"
    "    poly test :all-bricks\n"
    "    poly ws\n"
    "    poly ws get:keys\n"
    "    poly ws get:count\n"
    "    poly ws get:settings\n"
    "    poly ws get:user-input:args\n"
    "    poly ws get:user-input:args:0\n"
    "    poly ws get:settings:keys\n"
    "    poly ws get:components:keys\n"
    "    poly ws get:components:count\n"
    "    poly ws get:components:mycomp:lines-of-code-src\n"
    "    poly ws out:ws.edn"))

(defn print-help [color-mode]
  (println (help-text color-mode)))
