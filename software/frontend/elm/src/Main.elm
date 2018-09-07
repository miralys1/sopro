module Main exposing (..)

import Browser
import Html exposing (..)
import Html.Events exposing (..)
import Html.Attributes exposing (..)

main = Browser.element
    { init = init              -- init : flags -> ( model, Cmd msg )
    , view = view           -- view : model -> Html msg
    , update = update               -- update : msg -> model -> ( model, Cmd msg )
    , subscriptions = subscriptions -- subscriptions : model -> Sub msg
    }


testNodes : List Node
testNodes = [
    Node "3D" [] []
  , Node "4D" [] []
  , Node "5D" [] []
  , Node "3D-4D Converter" [] []
  , Node "3D-5D Converter" [] []
  ]

type alias Node = {
          name : String,
          ins  : List String,
          outs : List String
        }

type alias Model = {
            nodes : List Node
          , listVisible: Bool
        }

type Msg = AddNode Node | ListNodes

init : () -> (Model, Cmd Msg)
init _ = (Model testNodes False, Cmd.none)

update msg model =
    case msg of
      AddNode node ->
          (Model (node::model.nodes) model.listVisible, Cmd.none)
      ListNodes ->
          (Model model.nodes (not model.listVisible), Cmd.none)

subscriptions model = Sub.none

view model =
    div [] [
             button [ onClick (AddNode (Node "Test" [] []))] [text "Add Node"]
           , button [ onClick (ListNodes)] [text "List Nodes"]
           , div [] (if model.listVisible then [viewNodes model.nodes] else [])
           ]


viewNodes : List Node -> Html msg
viewNodes nodes =
    div [] (List.map ((\str -> li [class "list-group-item", draggable "true"] [text str]) << .name) nodes)
