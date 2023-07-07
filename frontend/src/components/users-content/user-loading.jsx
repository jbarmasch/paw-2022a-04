import Skeleton from '@mui/material/Skeleton';
import Box from '@mui/material/Box';


const UserLoading = () => (
    <>
        <Box className="user-card">
            <Skeleton width="50%" height="40px" variant="rectangular"/>
            <Skeleton sx={{marginTop: "7px"}} width="70%" height="40px" variant="rectangular"/>
            <Skeleton sx={{marginTop: "7px"}} width="40%" height="40px" variant="rectangular"/>
        </Box>
    </>
);


export default UserLoading