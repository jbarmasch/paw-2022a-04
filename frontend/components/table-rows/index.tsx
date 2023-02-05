import * as React from "react";
import {TicketType} from "../../types";

function TableRows({rowsData, deleteTableRows, handleChange, register}) {

    return (
            rowsData.map((data, index) => {
                // console.log(data)
                // console.log(index)
            const {ticketName, price, qty, booked, maxPerUser, starting, until} = data;
                // console.log("vamos por" + data["ticketName" + index])
            // let ticketName = data["ticketName" + index]
            //     let price = data["price" + index]
            //     let qty = data["price" + index]
            //     let booked = data["price" + index]
            //     let maxPerUser = data["price" + index]
            //     let starting = data["starting" + index]
            //     let until = data["until" + index]
            return (
                <tr key={index}>
                    <td>
                        {/*<input type="text" value={ticketName} onChange={(evnt) => (handleChange(index, evnt))}*/}
                        {/*       name="ticketName" className="form-control"/>*/}
                        <input type="text" {...register("ticketName" + index, { required: true })}
                               onChange={(evnt) => (handleChange(index, evnt))} value={ticketName}/>
                    </td>
                    <td><input type="number" {...register("price" + index, { required: true })} value={price}
                               onChange={(evnt) => (handleChange(index, evnt))}
                               className="form-control"/></td>
                    <td><input type="text" {...register("qty" + index, { required: true })} value={qty}
                               onChange={(evnt) => (handleChange(index, evnt))}
                               className="form-control"/></td>
                    <td><input type="number" {...register("booked" + index, { required: true })} value={booked}
                               onChange={(evnt) => (handleChange(index, evnt))}
                               className="form-control"/></td>
                    <td><input type="number" {...register("maxPerUser" + index, { required: true })} value={maxPerUser}
                               onChange={(evnt) => (handleChange(index, evnt))}
                               className="form-control"/></td>
                    <td><input type="datetime-local" {...register("starting" + index)}
                               value={starting} onChange={(evnt) => (handleChange(index, evnt))}
                               className="form-control"/></td>
                    <td>
                        {/*<input type="datetime-local" value={until} onChange={(evnt) => (handleChange(index, evnt))} name="until"*/}
                        {/*       className="form-control"/>*/}
                        <input type="datetime-local" {...register("until" + index)}
                               className="form-control" onChange={(evnt) => (handleChange(index, evnt))}
                               value={until}/>
                    </td>
                    <td>
                        <button className="btn btn-outline-danger" onClick={() => (deleteTableRows(index))}>x</button>
                    </td>
                </tr>
            )
        })
    )
}

export default TableRows;