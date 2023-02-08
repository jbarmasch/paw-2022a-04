import {useEffect, useMemo, useState} from 'react';
import Layout from '../layout';
import {server} from '../../utils/server';
import useSwr from "swr";
import MyEventLoading from "../../components/my-events-content/my-event-loading";
import {useForm, useFieldArray, Controller} from "react-hook-form";
import {Link, useHistory, useLocation} from 'react-router-dom'
import useFindPath from '../header'

const isEqualsJson = (oldTicket, newTicket) => {
    let oldKeys = Object.keys(oldTicket);
    let newKeys = Object.keys(newTicket);

    return oldKeys.length === newKeys.length && Object.keys(oldTicket).every(key => {
        return oldTicket[key] == newTicket[key]
    });
}

const getMinValue = (ticket) => {
    return ticket.hasOwnProperty("booked") && ticket.booked > 0 ? ticket.booked : 1
}

const MyEvent = (props) => {
    const prevLocation = useLocation();

    const [showBlock, setShowBlock] = useState('description');

    const fetcher = (url) => fetch(url).then((res) => res.json());

    const history = useHistory();
    let path = useFindPath();

    const [rowsData, setRowsData] = useState([]);
    const [index, setIndex] = useState(0);

    const { data : event, error : errorData } = useSwr(props.match.params.id ? `${server}/api/events/${props.match.params.id}` : null, fetcher)

    const [tickets, setTickets] = useState([]);

    useEffect(() => {
        if (event) {
            fetch(event.tickets)
                .then(r => {
                    if (r.status == 200) {
                        return r.json()
                    } else {
                        return
                    }
                })
                .then(d => {
                    console.log(d)
                    setTickets(d) 
                    setValue("tickets", tickets);
                })
        }
    }, [event])

    const { data : aux, error : error } = useSwr(event ? `${event.image}` : null, fetcher)

    const { register, control, handleSubmit, reset, watch, setValue, getValues, formState: {errors} } = useForm({
        defaultValues: {
            tickets: [{
                ticketName: "",
                price: "",
                qty: "",
                maxPerUser: "",
                starting: "",
                until: ""
            }]
        }
    });

    const { fields, append, prepend, remove, swap, move, insert } = useFieldArray(
        {
            control,
            name: "tickets"
        }
    );

    let accessToken;
    let refreshToken;
    if (typeof window !== 'undefined') {
        accessToken = localStorage.getItem("Access-Token");
        refreshToken = localStorage.getItem("Refresh-Token");
    }

    useEffect(() => {
        setValue("tickets", tickets);
    }, [tickets]);

    useEffect(() => {
        const fetchData = async (accessToken, refreshToken, pathname) => {
            let res = fetch(`${server}/api/users/test`, {
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                },
            })

            let aux = await res;
            if (aux.status == 200) {
                return;
            }

            res = fetch(`${server}/api/users/test`, {
                headers: {
                    'Authorization': `Bearer ${refreshToken}`
                },
            })

            aux = await res;
            if (aux.status == 200) {
                localStorage.setItem("Access-Token", aux.headers.get("Access-Token"))
                return;
            }

            if (path !== "/login") {
                history.push(`/login?redirectTo=${prevLocation.pathname}`);
            }
        }

        if (accessToken && refreshToken) {
            fetchData(accessToken, refreshToken, path)
        } else {
            const pathname = path
            if (path !== "/login") {
                history.push(`/login?redirectTo=${prevLocation.pathname}`);
            }
        }
    }, [accessToken, refreshToken]);

    if (error || errorData) return <p>No data</p>
    if (!aux || !event) return <MyEventLoading/>

    const onSubmit = async (data) => {
        console.log(data)

        console.log(rowsData)

        for (const x of rowsData) {
            await fetch(`${server}/api/tickets/${x}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                },
            })
        }

        let res
        let auxi = {
            tickets: []
        }

        for (const d of data.tickets) {
            if (d.ticketId) {
                const ticket = tickets.find(x => {
                    return x.ticketId == d.ticketId
                })

                console.log(ticket)
                console.log(d)

                const defaultValues = {
                    ticketId: ticket.ticketId,
                    self: ticket.self,
                    bookings: ticket.bookings,
                    event: ticket.event,
                    ticketName: ticket.ticketName,
                    price: ticket.price,
                    qty: ticket.qty,
                    booked: ticket.booked,
                    maxPerUser: ticket.maxPerUser,
                    starting: ticket.starting ? ticket.starting : "",
                    until: ticket.until ? ticket.starting : ""
                }

                const newTicket = { ...defaultValues};

                console.log(newTicket)

                let aux = JSON.parse(JSON.stringify(d));

                if (!isEqualsJson(d, newTicket)) {
                    console.log("equals")
                    res = await fetch(`${server}/api/tickets/${d.ticketId}`, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${accessToken}`
                        },
                        body: JSON.stringify(aux)
                    })
                }
            }
            else {
                auxi.tickets.push(d)
            }
        }

        let aux = JSON.parse(JSON.stringify(auxi));
        console.log(aux)

        res = await fetch(`${server}/api/events/${props.match.params.id}/tickets`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            },
            body: JSON.stringify(aux)
        })

        let json = await res;
    }

    return (
        <Layout>
            <section className="product-single">
                <div className="container">
                    <div className="product-single__content">
                        <img className={"product-gallery__image"} src={`data:image/png;base64,${aux.image}`} alt="My event image"/>
                    </div>

                    <form className="form" onSubmit={handleSubmit(onSubmit)}>
                        <table className="ticket-table ticket-table-input">
                            <thead>
                            <tr>
                                <th>Ticket name</th>
                                <th>Price</th>
                                <th>Quantity</th>
                                <th>Max p/ user</th>
                                <th>Starting</th>
                                <th>Until</th>
                                <th>
                                    <button
                                        type="button"
                                        onClick={() => {
                                            if (fields.length >= 5) {
                                                alert("Te quedaste sin tickets hermano")
                                                return;
                                            }
                                            append({
                                                ticketName: "",
                                                price: "",
                                                qty: "",
                                                maxPerUser: "",
                                                starting: "",
                                                until: ""
                                            });
                                        }}
                                    >
                                        Add
                                    </button>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                                {fields.map((item, index) => {
                                    return (
                                        <tr key={item.id}>
                                            <td>
                                                <input
                                                    // className={quicksand.className}
                                                    defaultValue={`${item.ticketName}`}
                                                    {...register(`tickets[${index}].ticketName`, { required: true })}
                                                    type="text"
                                                />
                                                {errors.tickets?.at(index)?.ticketName?.type === 'required' && <span>This field is required</span>}
                                            </td>
                                            <td>
                                                <input
                                                    defaultValue={`${item.price}`}
                                                    {...register(`tickets[${index}].price`, { required: true, min: 0 })}
                                                    type="number"
                                                />
                                                {errors.tickets?.at(index)?.price?.type === 'required' && <span>This field is required</span>}
                                                {errors.tickets?.at(index)?.price?.type === 'min' && <span>MIN!!!!</span>}
                                            </td>
                                            <td>
                                                <input
                                                    defaultValue={`${item.qty}`}
                                                    {...register(`tickets[${index}].qty`, {
                                                        required: true,
                                                        min: getMinValue(item)
                                                    }
                                                    )}
                                                    type="number"
                                                />
                                                {errors.tickets?.at(index)?.qty?.type === 'required' && <span>This field is required</span>}
                                                {errors.tickets?.at(index)?.qty?.type === 'min' && <span>MIN!!!!</span>}
                                            </td>
                                            <td>
                                                <input
                                                    defaultValue={`${item.maxPerUser}`}
                                                    {...register(`tickets[${index}].maxPerUser`, { required: true, min: 1, max: 10 })}
                                                    type="number"
                                                />
                                                {errors.tickets?.at(index)?.maxPerUser?.type === 'required' && <span>This field is required</span>}
                                                {errors.tickets?.at(index)?.maxPerUser?.type === 'min' && <span>MIN!</span>}
                                                {errors.tickets?.at(index)?.maxPerUser?.type === 'max' && <span>MAX!</span>}
                                            </td>
                                            <td>
                                                <input
                                                    defaultValue={`${item.starting}`}
                                                    {...register(`tickets[${index}].starting`)}
                                                    type="datetime-local"
                                                />
                                                {errors.tickets?.at(index)?.starting?.type && <span>This field is required</span>}
                                            </td>
                                            <td>
                                                <input
                                                    defaultValue={`${item.until}`}
                                                    {...register(`tickets[${index}].until`, {
                                                        validate: () => {
                                                            if (getValues(`tickets[${index}].starting`) && getValues(`tickets[${index}].until`)) {
                                                                return new Date(getValues(`tickets[${index}].starting`)).getTime() < new Date(getValues(`tickets[${index}].until`)).getTime()
                                                            }
                                                            return true;
                                                        }
                                                    })}
                                                    type="datetime-local"
                                                />
                                                {errors.tickets?.at(index)?.until?.type === 'required' && <span>This field is required</span>}
                                                {errors.tickets?.at(index)?.until?.type === 'validate' && <span>STARTING > UNTIL GILASTRUM</span>}
                                            </td>

                                            <td>
                                                <button type="button" onClick={() => {
                                                    if (item.ticketId) {
                                                        console.log(item.ticketId)
                                                        rowsData.push(item.ticketId)
                                                        setRowsData(rowsData)
                                                    }
                                                    remove(index)
                                                }}>
                                                    Delete
                                                </button>
                                            </td>
                                        </tr>
                                    );
                                })}
                            </tbody>
                        </table>
                        <input type="submit" />
                    </form>

                    <div className="product-single__info">
                        <div className="product-single__info-btns">
                            <button type="button" onClick={() => setShowBlock('description')}
                                    className={`btn btn--rounded ${showBlock === 'description' ? 'btn--active' : ''}`}>Description
                            </button>
                            <button type="button" onClick={() => setShowBlock('reviews')}
                                    className={`btn btn--rounded ${showBlock === 'reviews' ? 'btn--active' : ''}`}>Reviews
                                (2)
                            </button>
                        </div>
                    </div>
                </div>
            </section>
        </Layout>
    );
}

export default MyEvent
