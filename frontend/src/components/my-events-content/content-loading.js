import MyEventLoading from './my-event-loading';
import Skeleton from "@mui/material/Skeleton";
import EventItemLoading from "./my-event-loading";

const ProductsLoading = () => {
    return (
        <div className="event-page">
            <div style={{width: "100%"}}>
                <div className="event-content-intro">
                    <Skeleton width="140px" height="60px"/>
                    <div className="flex-row">
                        <Skeleton width="140px" height="60px"/>
                        <Skeleton width="140px" height="60px"/>
                    </div>
                </div>
                <section className="event-list my-event-list">
                    {[...Array(12)].map((e, i) => <EventItemLoading key={17 + i}/>)}
                </section>
            </div>
        </div>
    );
};

export default ProductsLoading