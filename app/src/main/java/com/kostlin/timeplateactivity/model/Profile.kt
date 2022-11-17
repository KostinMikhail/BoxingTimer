package com.kostlin.boxingtimer.model

class Profile {

        var roundTime : Int = 0
        var restTime : Int = 0
        var amountOfRounds: Int = 0
        var name : String = ""


        constructor(roundTime: Int, restTime: Int, amountOfRounds: Int, name: String){

            this.roundTime = roundTime
            this.restTime = restTime
            this.amountOfRounds = amountOfRounds
            this.name = name
        }



}
