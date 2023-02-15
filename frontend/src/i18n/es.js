const i18n_es = {
    app: "Embotellador",
    submit: "Enviar",
    fieldRequired: "Este campo es obligatorio",
    fieldInvalid: "Este campo es inválido",
    passwordMatch: "Las contraseñas no coinciden",
    passwordInvalid: "La contraseña es inválida",
    loading: "Cargando...",
    autocompleteNoOptions: "No hay opciones",
    filters: "Filtros",
    noData: "Sin datos",
    seeEvents: "Ver eventos",

    nav: {
        events: "Eventos",
        createEvent: "Crear evento",
        bookings: "Reservas",
        myEvents: "Mis eventos",
        account: "Cuenta",
        organizers: "Organizadores",
        profile: "Perfil",
        logout: "Cerrar sesión"
    },

    myEvents: {
        title: "Mis eventos",
        ticketDateError: "STARTING > UNTIL GILASTRUM",
        ticketPriceError: "El precio de un ticket no puede ser menor a 0",
        ticketQtyError: "La cantidad de tickets no puede ser menor a 1",
        ticketsPerUserError: "La cantidad de tickets por usuario debe estar entre 1 y 10",
        ticketsLeftError: "Te quedaste sin tickets hermano"
    },

    home: {
        phrase: "Vení a divertirte con nosotros!",
        page: "Inicio",
        events: "Eventos",
        next: "Siguiente",
        previous: "Anterior",
        recommended: "Eventos recomendados solo para vos",
        trending: "Conciertos populares en este momento",
        featured: "Ver eventos destacados",
        createTitle: "Creá tus eventos gratis",
        createDesc: "Usá BotPass para tener un alcance mayor",
        rangeTitle: "BotPass ofrece un amplio rango de eventos",
        rangeDesc: "Desde eventos deportivos hasta fiestas, BotPass lo tiene todo",
        bookTitle: "Reservá desde la comodidad de tu casa",
        bookDesc: "Nuestro sencillo sistema de reservas te permite reservar instantáneamente",
    },

    create: {
        name: "Nombre del evento",
        description: "Descripción",
        location: "Seleccionar ubicación",
        type: "Seleccionar tipo de evento",
        tags: "Seleccionar tags",
        hasMin: "Edad mínima",
        minAge: "Edad mín.",
        uploadFile: 'Subir foto de evento',
        date: 'Fecha del evento',
        dateError: "No se puede crear un evento que ya haya terminado"
    },

    event: {
        name: "Nombre del evento",
        description: "Descripción del evento",
        minPrice: "Desde",
        type: "Tipo del evento",
        location: "Ubicación",
        tags: "Tags",
        ticket: "Nombre del ticket",
        price: "Precio",
        quantity: "Cantidad",
        date: "Fecha",
        maxpuser: "Máx. por usuario",
        until: "Hasta",
        free: "Gratis",
        noTickets: "Sin tickets",
        selectQty: "Seleccione cantidad",
        starting: "Desde",
        similarPl: "Eventos similares",
        similarSi: "Evento similar",
        recommendedPl: "Eventos recomendados",
        recommendedSi: "Evento recomendado",
        upcomingPl: "No te pierdas los próximos eventos",
        upcomingSi: "No te pierdas el próximo evento",
        fewTicketsPl: "Estos eventos se están quedando sin entradas",
        fewTicketsSi: "Este evento se está quedando sin entradas",
        book: "Reservar",
        soldOut: "Agotado",
        minAge: "Edad mín.",
        minAgeText: "A partir de ",
        organizer: "Organizador",
        noEvents: "No events found"
    },

    login: {
        login: "Inicio de sesión",
        username: "Nombre de usuario",
        password: "Contraseña",
        keepMe: "Mantener sesión inicada",
        forgot: "Olvidaste tu contraseña?",
        signIn: "Iniciar sesión",
        notAMember: "No sos miembro?",
        signUp: "Registrarse",
        notFound: "Usuario o contraseña incorrectos"
    },

    filter: {
        title: "Eventos",
        locations: "Seleccionar ubicaciones",
        types: "Seleccionar tipos",
        tags: "Seleccionar tags",
        sortBy: "Ordenar por",
        order: "Orden",
        price: "Price",
        apply: "Apply",
        minPrice: "Min. price",
        maxPrice: "Max. price",
        username: "Nombre de usuario",
        rating: "Puntuación",
        ascending: "Ascendente",
        descending: "Descendente",
        soldOut: "Agotado",
        noTickets: "Sin tickets",
        advancedOptions: "Opciones avanzadas",
        minPriceError: "El precio mínimo debe ser mayor o igual a 0",
        maxPriceError: "El precio máximo debe ser mayor o igual a 0 y al precio mínimo",
        rangePriceError: "El precio máximo no puede ser menor al mínimo",
        clear: "Limpiar filtros"
    },

    bookings: {
        title: "Reservas",
        booking: "Reserva",
        all: "Todas las reservas",
        cancel: "Cancelar",
        ticket: "Ticket",
        qty: "Cantidad",
        price: "Precio",
        rate: "Valorar",
        cancelMessage: "¿Está seguro de que desea cancelar su reserva?",
        accept: "Aceptar",
        confirm: "Confirmar",
        invalidate: "Invalidar"
    },


    stats: {
        stats: "Estadísticas",
        eventsAttended: "Eventos reservados",
        ticketsBooked: "Entradas reservadas",
        favType: "Tipo de evento favorito",
        favLocation: "Ubicación favorita",
    },

    register: {
        create: "Registrate para crear eventos y reservar entradas",
        name: "Nombre de usuario",
        pass: "Contraseña",
        repeat: "Repetir contraseña"
    },

    footer: {
        text: "BotPass conecta organizadores de eventos con la gente, dando visibilidad a los eventos menos conocidos y al mismo tiempo brindando una plataforma para que los organizadores puedan vender entradas y ver sus estadísticas.",
        events: "Events",
        fewTickets: "Few tickets",
        recommended: "Recommended",
        popular: "Popular",
        contact: "Contacto",
    },

    organizer: {
        organizers: "Organizadores",
        seeEvents: "Ver eventos",
        noEvents: "Sin eventos",
        searchOrganizer: "Buscar organizador",
        noOrganizers: "No se encontraron organizadores"
    },

    thankYou: {
        phrase: "Gracias por reservar con BotPass"
    }
};

export default i18n_es;