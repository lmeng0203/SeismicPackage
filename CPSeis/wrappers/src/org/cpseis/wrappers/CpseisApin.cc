//////// DO NOT EDIT THIS FILE - it is machine generated ////////

#include "CpseisApin.hh"

//------------------ fortran spelling adjustments --------------------//
//------------------ fortran spelling adjustments --------------------//
//------------------ fortran spelling adjustments --------------------//

#if NEED_UNDERSCORE
#define apin_wrap_create             apin_wrap_create_
#define apin_wrap_delete             apin_wrap_delete_
#define apin_wrap_update             apin_wrap_update_
#define apin_wrap_wrapup             apin_wrap_wrapup_
#define apin_wrap_oneset             apin_wrap_oneset_
#define apin_wrap_twosets            apin_wrap_twosets_
#elif NEED_CAPITALS
#define apin_wrap_create             APIN_WRAP_CREATE
#define apin_wrap_delete             APIN_WRAP_DELETE
#define apin_wrap_update             APIN_WRAP_UPDATE
#define apin_wrap_wrapup             APIN_WRAP_WRAPUP
#define apin_wrap_oneset             APIN_WRAP_ONESET
#define apin_wrap_twosets            APIN_WRAP_TWOSETS
#endif

//----------------------- fortran prototypes -------------------------//
//----------------------- fortran prototypes -------------------------//
//----------------------- fortran prototypes -------------------------//

extern "C"
    {
    CpseisBase::ModuleCreate   apin_wrap_create;
    CpseisBase::ModuleDestroy  apin_wrap_delete;
    CpseisBase::ModuleUpdate   apin_wrap_update;
    CpseisBase::ModuleWrapup   apin_wrap_wrapup;
    CpseisBase::ModuleOneset   apin_wrap_oneset;
    CpseisBase::ModuleTwosets  apin_wrap_twosets;
    }

//------------------------ constructor -------------------------------//
//------------------------ constructor -------------------------------//
//------------------------ constructor -------------------------------//

CpseisApin::CpseisApin() : CpseisBase ("APIN",
                                       apin_wrap_create,
                                       apin_wrap_delete,
                                       apin_wrap_update,
                                       apin_wrap_wrapup,
                                       apin_wrap_oneset,
                                       apin_wrap_twosets) {}

//------------------------------ end ---------------------------------//
//------------------------------ end ---------------------------------//
//------------------------------ end ---------------------------------//
