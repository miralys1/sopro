module Model exposing (..)

type Msg = Move Node | DropNode String | Delete Node | InvalidLocation | AddNode Node

type alias Model = {
          nodes : List Node,
          moveNode : Maybe Node
        }

type alias Node = {
          name : String,
          ins : List String,
          outs : List String
        }

initModel : Model -> (Model, Cmd msg)
initModel flags = ( Model [] Nothing, Cmd.none )
