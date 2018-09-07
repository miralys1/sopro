module Views exposing (..)

import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (..)
import Browser exposing (..)

import Model exposing (..)
-- import EventHelpers exposing (..)

-- nodeView :

documentView model =
  let
         body = div [] [nav [ class "navbar navbar-dark" ]
                            [ div [class "container-fluid"]
                                  [div [class "navbar-header"]
                                        [text "Hello World!"]
                                  ]
                            ]
                       , h1 [] [text "Test"]
                       ]
         title = "SWARM-Composer"
         bod2 = div []
                    [   div [] [ text (String.fromInt 42) ]
                      , button [ onClick (AddNode (Node "3d Modeller" [] []))] [text "Add node"]
                      , div [] [ text (String.fromInt (List.length model.nodes)) ]
                    ]

  in Document title [body, bod2]
