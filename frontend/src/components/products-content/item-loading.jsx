import Skeleton from '@mui/material/Skeleton';
import Box from '@mui/material/Box';


const EventLoading = () => (
    <>
        {/* <div style={{width: "200px", height: "200px", backgroundColor: "black"}}></div> */}
        <Box className="event-card">
            <div className="event-card-action">
                <Skeleton variant="rectangular" className="event-card-image"/>
                {/* <Skeleton variant="rectangular" width={210} height={118} /> */}
                <Box width="100%">
                    <Skeleton />
                    <Skeleton width="60%" />
                </Box>
            </div>
        </Box>
    </>
);


export default EventLoading