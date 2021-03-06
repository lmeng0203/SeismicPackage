//////// DO NOT EDIT THIS FILE - it is machine generated ////////

#include "CpseisSpike.hh"

//------------------ fortran spelling adjustments --------------------//
//------------------ fortran spelling adjustments --------------------//
//------------------ fortran spelling adjustments --------------------//

#if NEED_UNDERSCORE
#define spike_wrap_create             spike_wrap_create_
#define spike_wrap_delete             spike_wrap_delete_
#define spike_wrap_update             spike_wrap_update_
#define spike_wrap_wrapup             spike_wrap_wrapup_
#define spike_wrap_oneset             spike_wrap_oneset_
#define spike_wrap_twosets            spike_wrap_twosets_
#elif NEED_CAPITALS
#define spike_wrap_create             SPIKE_WRAP_CREATE
#define spike_wrap_delete             SPIKE_WRAP_DELETE
#define spike_wrap_update             SPIKE_WRAP_UPDATE
#define spike_wrap_wrapup             SPIKE_WRAP_WRAPUP
#define spike_wrap_oneset             SPIKE_WRAP_ONESET
#define spike_wrap_twosets            SPIKE_WRAP_TWOSETS
#endif

//----------------------- fortran prototypes -------------------------//
//----------------------- fortran prototypes -------------------------//
//----------------------- fortran prototypes -------------------------//

extern "C"
    {
    CpseisBase::ModuleCreate   spike_wrap_create;
    CpseisBase::ModuleDestroy  spike_wrap_delete;
    CpseisBase::ModuleUpdate   spike_wrap_update;
    CpseisBase::ModuleWrapup   spike_wrap_wrapup;
    CpseisBase::ModuleOneset   spike_wrap_oneset;
    CpseisBase::ModuleTwosets  spike_wrap_twosets;
    }

//------------------------ constructor -------------------------------//
//------------------------ constructor -------------------------------//
//------------------------ constructor -------------------------------//

CpseisSpike::CpseisSpike() : CpseisBase ("SPIKE",
                                       spike_wrap_create,
                                       spike_wrap_delete,
                                       spike_wrap_update,
                                       spike_wrap_wrapup,
                                       spike_wrap_oneset,
                                       spike_wrap_twosets) {}

//------------------------------ end ---------------------------------//
//------------------------------ end ---------------------------------//
//------------------------------ end ---------------------------------//
