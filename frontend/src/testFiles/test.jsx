import React, {useState, useEffect} from "react";


export const test =()=>{
const [time, setTime]= useState(0);
return(
    <div>
    <button className="btn " onClick={setTime(time+1)}> click</button>
    <h1>{time}</h1>
    </div>
);
}
