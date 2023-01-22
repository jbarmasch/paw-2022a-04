import {useState} from 'react';
import List from './list';

const BookingsContent = ({userId}) => {
    const [orderProductsOpen, setOrderProductsOpen] = useState(false);

    return (
        <section className="products-content">
            <div className="products-content__intro">
                <h2>Your bookings</h2>
                <button type="button" onClick={() => setOrderProductsOpen(!orderProductsOpen)}
                        className="products-filter-btn"><i className="icon-filters"></i></button>
                <form className={`products-content__filter ${orderProductsOpen ? 'products-order-open' : ''}`}>
                    <div className="products__filter__select">
                        <h4>Sort by: </h4>
                        <div className="select-wrapper">
                            <select>
                                <option>Date</option>
                            </select>
                        </div>
                    </div>
                </form>
            </div>

            <List userId={userId}/>
        </section>
    );
};

export default BookingsContent
  